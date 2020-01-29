package com.airwallex.codechallenge.service.impl;

import com.airwallex.codechallenge.alerts.Alerter;
import com.airwallex.codechallenge.alerts.context.AlerterContext;
import com.airwallex.codechallenge.alerts.model.Alert;
import com.airwallex.codechallenge.exceptions.NoAlerterConfiguredException;
import com.airwallex.codechallenge.input.CurrencyConversionRate;
import com.airwallex.codechallenge.service.AlertService;

import java.util.*;
import java.util.function.Function;

public class AlertServiceImpl implements AlertService {

  @Override
  public void process(
      List<CurrencyConversionRate> rates, List<Alerter> alerters, Function<Alert, Boolean> fn) {
    if (alerters.isEmpty()) {
      throw new NoAlerterConfiguredException("No alerters configured");
    }

    int maxWindow =
        alerters.stream()
            .mapToInt(value -> value.requiredPeriods())
            .max()
            .orElseThrow(NoSuchElementException::new);

    // Maintain a map of sliding window for each of the currency pair
    Map<String, LinkedList<CurrencyConversionRate>> window = new HashMap<>();
    List<AlerterContext> running = new ArrayList<>();
    for (CurrencyConversionRate rate : rates) {
      if (!window.containsKey(rate.getCurrencyPair())) {
        window.put(rate.getCurrencyPair(), new LinkedList<>());
      }
      window.get(rate.getCurrencyPair()).add(rate);

      // Create currency pair sliding window
      LinkedList<CurrencyConversionRate> currencyPairWindow = window.get(rate.getCurrencyPair());
      for (Alerter alerter : alerters) {
        // If the currency sliding window contains enough periods, invoke the alerters for a
        // plausible alert.
        if (currencyPairWindow.size() >= alerter.requiredPeriods()) {
          AlerterContext alerterContext =
              new AlerterContext(alerters.indexOf(alerter), rate.getCurrencyPair(), alerter, 0);
          int ctxIndex = running.indexOf(alerterContext);
          if (ctxIndex >= 0) {
            alerterContext = running.get(ctxIndex);
          } else {
            running.add(alerterContext);
          }
          fn.apply(
              alerterContext
                  .getAlerter()
                  .doAlert(
                      rate.getCurrencyPair(),
                      new LinkedList<>(
                          currencyPairWindow.subList(
                              alerterContext.getOffSet(),
                              alerterContext.getOffSet() + alerter.requiredPeriods()))));
          alerterContext.incrementOffSetBy(1);
        }
      }

      // If currency pair sliding window is greater than max window period, then trim it.
      if (currencyPairWindow.size() > maxWindow) {
        running.forEach(
            context -> {
              if (context.getCurrencyPair().equals(rate.getCurrencyPair())) {
                context.decrementOffSetBy(1);
              }
            });
        currencyPairWindow.remove();
      }
    }
  }
}
