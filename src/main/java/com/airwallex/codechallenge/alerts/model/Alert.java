package com.airwallex.codechallenge.alerts.model;

import com.airwallex.codechallenge.Writer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.Instant;

public class Alert {
  private final Instant timestamp;

  private final String currencyPair;

  private final String alert;

  private final Long seconds;

  public Alert(Instant timestamp, String currencyPair, String alert, Long seconds) {
    this.timestamp = timestamp;
    this.currencyPair = currencyPair;
    this.alert = alert;
    this.seconds = seconds;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public String getCurrencyPair() {
    return currencyPair;
  }

  public String getAlert() {
    return alert;
  }

  public Long getSeconds() {
    return seconds;
  }

  @JsonIgnore
  public String toJSONString() {
    try {
      return new Writer().toJSONString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
