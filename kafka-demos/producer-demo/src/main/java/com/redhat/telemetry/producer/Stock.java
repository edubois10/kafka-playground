package com.redhat.telemetry.producer;

public class Stock {
    private String name;
    private double initialPrice;
    private double volatility;
  
    public Stock(String name, double initialPrice, double volatility) {
      this.name = name;
      this.initialPrice = initialPrice;
      this.volatility = volatility;
    }
  
    public String getName() {
      return name;
    }
  
    public double getInitialPrice() {
      return initialPrice;
    }
  
    public double getVolatility() {
      return volatility;
    }
  }
