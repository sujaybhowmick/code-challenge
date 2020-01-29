package com.airwallex.codechallenge.service;

import com.airwallex.codechallenge.alerts.Alerter;
import com.airwallex.codechallenge.alerts.model.Alert;
import com.airwallex.codechallenge.input.CurrencyConversionRate;

import java.util.List;
import java.util.function.Function;

public interface AlertService {
  void process(
      List<CurrencyConversionRate> rates, List<Alerter> alerters, Function<Alert, Boolean> fn);
}
