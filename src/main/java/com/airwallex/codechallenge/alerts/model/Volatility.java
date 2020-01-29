package com.airwallex.codechallenge.alerts.model;

import java.time.Instant;

public class Volatility {
  private final Instant start;

  private final boolean up;

  public Volatility(boolean up, Instant start) {
    this.up = up;
    this.start = start;
  }

  public Boolean isUp() {
    return up;
  }

  public Instant getStart() {
    return start;
  }
}
