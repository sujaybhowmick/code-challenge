package com.airwallex.codechallenge;

import com.airwallex.codechallenge.alerts.Alerter;
import com.airwallex.codechallenge.alerts.MovingAverageAlerter;
import com.airwallex.codechallenge.alerts.VolatilityAlerter;
import com.airwallex.codechallenge.alerts.model.Alert;
import com.airwallex.codechallenge.input.CurrencyConversionRate;
import com.airwallex.codechallenge.input.Reader;
import com.airwallex.codechallenge.service.AlertService;
import com.airwallex.codechallenge.service.impl.AlertServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class App {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: Program <input file json>");
      System.exit(1);
    }
    Reader reader = new Reader();
    List<CurrencyConversionRate> rates = reader.read(args[0]).collect(Collectors.toList());
    List<Alerter> alerters = new ArrayList<>();
    alerters.add(new MovingAverageAlerter(10.0, 2));
    alerters.add(new VolatilityAlerter(900, 60));
    List<Alert> alerts = new ArrayList<>();
    Function<Alert, Boolean> fn = alert -> alert != null && alerts.add(alert);
    AlertService service = new AlertServiceImpl();
    service.process(rates, alerters, fn);
    alerts.forEach(
        alert -> {
          System.out.println(alert.toJSONString());
        });
  }
}
