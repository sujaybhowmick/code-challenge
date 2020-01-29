package com.airwallex.codechallenge.alerts;

import com.airwallex.codechallenge.alerts.model.Alert;
import com.airwallex.codechallenge.alerts.model.Volatility;
import com.airwallex.codechallenge.input.CurrencyConversionRate;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

public class VolatilityAlerter implements Alerter {

  private final long minTrendLength;

  private final int throttleInSeconds;

  private CurrencyConversionRate previousRate;

  private Instant lastAlertTime;

  private Volatility volatility;

  public VolatilityAlerter(long minTrendLength, int throttleInSeconds) {
    this.minTrendLength = minTrendLength;
    this.throttleInSeconds = throttleInSeconds;
  }

  @Override
  public Alert doAlert(String currencyPair, LinkedList<CurrencyConversionRate> rates) {
    CurrencyConversionRate currentRate = rates.getFirst();
    if (previousRate == null) {
      previousRate = currentRate;
    } else if (currentRate.getRate() != previousRate.getRate()) {
      boolean up = currentRate.getRate() > previousRate.getRate();
      if (volatility == null) {
        volatility = new Volatility(up, previousRate.getTimestamp());
      } else if (up ^ volatility.isUp()) {
        volatility = new Volatility(up, previousRate.getTimestamp());
      }
      long trendLength =
          Duration.between(volatility.getStart(), currentRate.getTimestamp()).getSeconds();
      previousRate = currentRate;

      if (trendLength >= minTrendLength) {
        if (lastAlertTime == null
            || Duration.between(lastAlertTime, currentRate.getTimestamp()).getSeconds()
                >= throttleInSeconds) {
          lastAlertTime = currentRate.getTimestamp();
          return new Alert(
              currentRate.getTimestamp(),
              currencyPair,
              volatility.isUp() ? "rising" : "falling",
              trendLength);
        }
      }
    }
    return null;
  }

  @Override
  public int requiredPeriods() {
    return 1;
  }

  @Override
  public Alerter getInstance() {
    return new VolatilityAlerter(this.minTrendLength, this.throttleInSeconds);
  }
}
