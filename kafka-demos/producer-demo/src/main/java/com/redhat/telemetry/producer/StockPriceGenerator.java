package com.redhat.telemetry.producer;

import java.util.Random;

public class StockPriceGenerator {
  private static final double INITIAL_PRICE = 100.0;

  private Random random;
  private double currentPrice;
  private double average;
  private double volatility;

  /*
  public StockPriceGenerator(double average, double volatility) {
    random = new Random();
    currentPrice = INITIAL_PRICE;
    this.average = average;
    this.volatility = volatility;
  }*/

  public StockPriceGenerator(Stock stock) {
    random = new Random();
    currentPrice = stock.getInitialPrice();
    volatility = stock.getVolatility();
  }

  public double generateNextPrice() {
    double noise = random.nextGaussian();
    double priceChange = currentPrice * volatility * noise;
    double priceRatio = 1.0 + priceChange / currentPrice;
    currentPrice *= priceRatio;
    return currentPrice;
  }

  public double getVolatility() {
    return volatility;
  }
}