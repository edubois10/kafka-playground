package com.redhat.telemetry.producer;

import java.util.Random;

public class Sp500StockNameGenerator {
  private static final Stock[] STOCKS = {
      new Stock("AAPL", 200.0, 0.1),
      new Stock("GOOG", 300.0, 0.15),
      new Stock("AMZN", 400.0, 0.2),
      new Stock("MSFT", 150.0, 0.08),
      new Stock("FB", 100.0, 0.05),
      new Stock("TSLA", 500.0, 0.25),
      new Stock("NVDA", 250.0, 0.12),
      new Stock("JPM", 120.0, 0.06),
      new Stock("JNJ", 110.0, 0.04),
      new Stock("BABA", 150.0, 0.07)
  };

  private Random random;

  public Sp500StockNameGenerator() {
    random = new Random();
  }

  public Stock generateStockName() {
    int index = random.nextInt(STOCKS.length);
    return STOCKS[index];
  }
}
