package com.airwallex.codechallenge.input;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class MockInput {
  private int periods;
  private int increment;
  private double startRate;

  private MockInput(int increment, int periods, double startRate) {
    this.periods = periods;
    this.increment = increment;
    this.startRate = startRate;
  }

  public List<CurrencyConversionRate> getLinearRatesWithIncrement() {
    List<CurrencyConversionRate> rates = new LinkedList<>();
    IntStream.range(0, periods)
        .forEach(
            i -> {
              if (i == 0) {
                rates.add(new CurrencyConversionRate(Instant.now(), "USDAUD", this.startRate));
              } else {
                CurrencyConversionRate previous = rates.get(i - 1);
                rates.add(
                    new CurrencyConversionRate(
                        previous.getTimestamp().plusSeconds(1),
                        previous.getCurrencyPair(),
                        previous.getRate() + (increment++)));
              }
            });
    return rates;
  }

  public List<CurrencyConversionRate> getRatesWithNoChange() {
    List<CurrencyConversionRate> rates = new ArrayList<>();
    IntStream.range(0, periods)
        .forEach(
            i -> {
              if (i == 0) {
                rates.add(new CurrencyConversionRate(Instant.now(), "USDAUD", startRate));
              } else {
                CurrencyConversionRate previous = rates.get(i - 1);
                rates.add(
                    new CurrencyConversionRate(
                        previous.getTimestamp().plusSeconds(1),
                        previous.getCurrencyPair(),
                        previous.getRate()));
              }
            });
    return rates;
  }

  public List<CurrencyConversionRate> getRatesWithLessThan10PercentChange() {
    List<CurrencyConversionRate> rates = new ArrayList<>();
    IntStream.range(0, periods)
        .forEach(
            i -> {
              if (i == 0) {
                rates.add(new CurrencyConversionRate(Instant.now(), "USDAUD", startRate));
              } else {
                CurrencyConversionRate previous = rates.get(i - 1);
                rates.add(
                    new CurrencyConversionRate(
                        previous.getTimestamp().plusSeconds(1),
                        previous.getCurrencyPair(),
                        previous.getRate() + (increment++ / 100.0)));
              }
            });
    return rates;
  }

  public List<CurrencyConversionRate> getRatesRisingFalling() {
    List<CurrencyConversionRate> rates = new ArrayList<>();
    IntStream.range(0, periods)
        .forEach(
            i -> {
              if (i == 0) {
                rates.add(new CurrencyConversionRate(Instant.now(), "USDAUD", startRate));
              } else {
                CurrencyConversionRate previous = rates.get(i - 1);
                if (i <= rates.size() % 2) {
                  BigDecimal bd = BigDecimal.valueOf(previous.getRate() + (increment++ / 100.00));
                  bd = bd.setScale(2, RoundingMode.HALF_UP);
                  rates.add(
                      new CurrencyConversionRate(
                          previous.getTimestamp().plusSeconds(1),
                          previous.getCurrencyPair(),
                          bd.doubleValue()));
                } else {
                  BigDecimal bd = BigDecimal.valueOf(previous.getRate() - (increment++ / 100.00));
                  bd = bd.setScale(2, RoundingMode.HALF_UP);

                  rates.add(
                      new CurrencyConversionRate(
                          previous.getTimestamp().plusSeconds(1),
                          previous.getCurrencyPair(),
                          bd.doubleValue()));
                }
              }
            });
    return rates;
  }

  public static class MockInputBuilder {
    private int periods;
    private int increment;
    private double startRate;

    public MockInputBuilder increment(int increment) {
      this.increment = increment;
      return this;
    }

    public MockInputBuilder periods(int periods) {
      this.periods = periods;
      return this;
    }

    public MockInputBuilder startRate(double startRate) {
      this.startRate = startRate;
      return this;
    }

    public MockInput build() {
      return new MockInput(this.increment, this.periods, this.startRate);
    }
  }
}
