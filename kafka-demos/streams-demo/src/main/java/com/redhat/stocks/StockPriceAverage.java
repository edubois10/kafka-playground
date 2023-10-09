package com.redhat.stocks;

// A class to store the average stock price and count for each stock symbol
public class StockPriceAverage {

    private Double sum;
    private int count;

    public StockPriceAverage() {
        this.sum = 0.0;
        this.count = 0;
    }

    public StockPriceAverage(Double sum, int count) {
        this.sum = sum;
        this.count = count;
    }

    public Double getSum() {
        return sum;
    }

    public int getCount() {
        return count;
    }

    public Double getAverageValue() {
        if (count == 0) {
            return 0.0;
        } else {
            return sum / count;
        }
    }

    public StockPriceAverage addValue(double value) {
        sum += value;
        count++;
        return this;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(sum, count);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StockPriceAverage)) {
            return false;
        }
        StockPriceAverage other = (StockPriceAverage) obj;
        return java.util.Objects.equals(sum, other.sum) && count == other.count;
    }
}
