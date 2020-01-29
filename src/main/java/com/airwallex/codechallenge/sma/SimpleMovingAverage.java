package com.airwallex.codechallenge.sma;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleMovingAverage {

  private final Queue<Double> dataSet = new LinkedList<>();
  private int period;
  private double sum;

  public SimpleMovingAverage(int period) {
    this.period = period;
  }

  public void addData(double num) {
    sum += num;
    dataSet.add(num);

    if (dataSet.size() > period) {
      sum -= dataSet.remove();
    }
  }

  public double getMean() {
    return sum / period;
  }
}
