package com.airwallex.codechallenge.alerts;

import com.airwallex.codechallenge.alerts.model.Alert;
import com.airwallex.codechallenge.input.CurrencyConversionRate;
import com.airwallex.codechallenge.sma.SimpleMovingAverage;

import java.util.LinkedList;
import java.util.List;

public class MovingAverageAlerter implements Alerter {
  private final double percentRateThreshold;
  private final int requiredPeriods;

  public MovingAverageAlerter(double percentRateThreshold, int period) {
    this.requiredPeriods = period;
    this.percentRateThreshold = percentRateThreshold;
  }

  @Override
  public Alert doAlert(String currencyPair, LinkedList<CurrencyConversionRate> rates) {
    final SimpleMovingAverage sma = new SimpleMovingAverage(rates.size() - 1);
    List<CurrencyConversionRate> averageValues = rates.subList(0, rates.size() - 1);
    averageValues.stream().forEach(i -> sma.addData(i.getRate()));
    CurrencyConversionRate latestRate = rates.getLast();
    double average = sma.getMean();
    double difference =
        Math.abs((average - latestRate.getRate()) / ((average + latestRate.getRate()) / 2));
    if (difference >= (this.percentRateThreshold / 100)) {
      return new Alert(latestRate.getTimestamp(), currencyPair, "spotChange", null);
    } else {
      return null;
    }
  }

  public int requiredPeriods() {
    return requiredPeriods;
  }

  @Override
  public Alerter getInstance() {
    return new MovingAverageAlerter(this.percentRateThreshold, this.requiredPeriods);
  }
}
