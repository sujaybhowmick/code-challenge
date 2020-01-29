package com.airwallex.codechallenge.alerts.context;

import com.airwallex.codechallenge.alerts.Alerter;

public class AlerterContext {
  private final int id;

  private final String currencyPair;

  private final Alerter alerter;

  private int offSet;

  public AlerterContext(int id, String currencyPair, Alerter alerter, int offSet) {
    this.id = id;
    this.currencyPair = currencyPair;
    this.alerter = alerter;
    this.offSet = offSet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AlerterContext that = (AlerterContext) o;

    if (id != that.id) return false;
    if (!currencyPair.equals(that.currencyPair)) return false;
    if (!alerter.getClass().equals(alerter.getClass())) return false;
    return alerter.equals(that.alerter);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + currencyPair.hashCode();
    result = 31 * result + alerter.hashCode();
    return result;
  }

  public int getId() {
    return id;
  }

  public String getCurrencyPair() {
    return currencyPair;
  }

  public Alerter getAlerter() {
    return alerter;
  }

  public int getOffSet() {
    return offSet;
  }

  public void incrementOffSetBy(int offSet) {
    this.offSet += offSet;
  }

  public void decrementOffSetBy(int offSet) {
    this.offSet -= offSet;
  }
}
