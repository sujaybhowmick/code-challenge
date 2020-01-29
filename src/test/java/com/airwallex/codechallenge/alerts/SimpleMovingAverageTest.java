package com.airwallex.codechallenge.alerts;

import com.airwallex.codechallenge.sma.SimpleMovingAverage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleMovingAverageTest {

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void getMean() {
    SimpleMovingAverage sma = new SimpleMovingAverage(3);
    IntStream.range(0, 3)
        .forEach(
            i -> {
              sma.addData(i + i * 1);
            });
    assertEquals(2.0, sma.getMean());
  }
}
