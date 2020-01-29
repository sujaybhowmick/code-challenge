package com.airwallex.codechallenge.service.impl;

import com.airwallex.codechallenge.alerts.Alerter;
import com.airwallex.codechallenge.alerts.MovingAverageAlerter;
import com.airwallex.codechallenge.alerts.VolatilityAlerter;
import com.airwallex.codechallenge.alerts.model.Alert;
import com.airwallex.codechallenge.input.CurrencyConversionRate;
import com.airwallex.codechallenge.input.MockInput;
import com.airwallex.codechallenge.service.AlertService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AlertServiceTest {

  private MockInput mockInput;
  private AlertService alertService;
  private List<Alerter> alerters;

  @BeforeEach
  void setUp() {
    this.alertService = new AlertServiceImpl();
    this.alerters = new ArrayList<>();
    this.alerters.add(new MovingAverageAlerter(10.0, 2));
    this.alerters.add(new VolatilityAlerter(5, 11));
  }

  @AfterEach
  void tearDown() {
    mockInput = null;
    alerters = null;
  }

  @Test
  void testFor10PercentChangeAlert() {
    mockInput = new MockInput.MockInputBuilder().increment(1).periods(5).startRate(1.00).build();
    List<CurrencyConversionRate> rates = mockInput.getLinearRatesWithIncrement();
    final List<Alert> alerts = new ArrayList<>();
    this.alertService.process(
        rates,
        alerters,
        alert -> {
          if (alert != null) {
            return alerts.add(alert);
          }
          return false;
        });
    assertTrue(alerts.size() > 0);
  }

  @Test
  void testForNoChange() {
    mockInput = new MockInput.MockInputBuilder().increment(1).periods(4).startRate(1.00).build();
    List<CurrencyConversionRate> rates = mockInput.getRatesWithNoChange();
    final List<Alert> alerts = new ArrayList<>();
    this.alertService.process(
        rates,
        alerters,
        alert -> {
          if (alert != null) {
            return alerts.add(alert);
          }
          return false;
        });
    assertTrue(alerts.size() == 0);
  }

  @Test
  void testForVolatilityAlerts() {
    mockInput = new MockInput.MockInputBuilder().increment(10).periods(10).startRate(1.0).build();
    List<CurrencyConversionRate> rates = mockInput.getRatesRisingFalling();
    final List<Alert> alerts = new ArrayList<>();
    this.alertService.process(
        rates,
        alerters,
        alert -> {
          if (alert != null) {
            return alerts.add(alert);
          }
          return false;
        });
    assertTrue(alerts.size() > 0);
  }

  @Test
  void testForNoVolatilityAlerts() {
    mockInput = new MockInput.MockInputBuilder().increment(1).periods(10).startRate(1.0).build();
    List<CurrencyConversionRate> rates = mockInput.getRatesWithNoChange();
    final List<Alert> alerts = new ArrayList<>();
    this.alertService.process(
        rates,
        alerters,
        alert -> {
          if (alert != null) {
            return alerts.add(alert);
          }
          return false;
        });
    assertTrue(alerts.size() == 0);
  }
}
