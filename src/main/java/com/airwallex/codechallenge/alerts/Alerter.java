package com.airwallex.codechallenge.alerts;

import com.airwallex.codechallenge.alerts.model.Alert;
import com.airwallex.codechallenge.input.CurrencyConversionRate;

import java.util.LinkedList;

public interface Alerter {
  Alert doAlert(String currencyPair, LinkedList<CurrencyConversionRate> rates);

  int requiredPeriods();

  Alerter getInstance();
}
