### Question
Can you show an example of a quarkus application using kafka KTables as a KTable-Stream with incoming records being ("Stock", "CurrentPrice", "Index")
Here is an example of a Quarkus application using Kafka KTable as a KTable-Stream with incoming records being ("Stock", "CurrentPrice", "Index"):

````
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KTable;

public class StockPriceApplication {

    public static void main(String[] args) {
        StreamsBuilder builder = new StreamsBuilder();
        KTable<String, StockPrice> stockPrices = builder.table("stock-prices");

        stockPrices.toStream()
                   .filter((stock, price) -> price.getIndex().equals("NASDAQ"))
                   .foreach((stock, price) -> System.out.println("Stock: " + stock + " - Price: " + price.getCurrentPrice()));

        // start the stream
    }

    static class StockPrice {
        private String currentPrice;
        private String index;

        // getters and setters
    }
}
````
In this example, we're using the StreamsBuilder to create a KTable named stockPrices which is connected to a Kafka topic with the same name. We're then filtering the stream to only include stock prices for the NASDAQ index and printing out the stock symbol and current price for each record that matches the filter.


### Question
Can you show what an input to the stream looks like?
Here is an example of what an input to the stream might look like:

Copy code
("AAPL", new StockPrice("143.21", "NASDAQ"))
("GOOG", new StockPrice("2,636.23", "NASDAQ"))
("AMZN", new StockPrice("3,294.21", "NASDAQ"))
("TSLA", new StockPrice("834.12", "NASDAQ"))
Each input is a key-value pair, where the key is the stock symbol (e.g. "AAPL" for Apple Inc.) and the value is an instance of the StockPrice class, which contains the current price and the index (e.g. "NASDAQ") of the stock.

It's important to note that the inputs are being sent to the kafka topic 'stock-prices' and the KTable is connected to that topic to consume the data.

Also, you can use the Kafka-producer to send this data to the topic and consume it using ktable.


### Question

Instead of using the index value, can you change the index by a counter that would count how many times a given stock was updated? and show an example how aggregation would work then.
Sure, we can change the index value to a counter that counts how many times a given stock was updated. Here's an example:

```
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KTable;

public class StockPriceApplication {

    public static void main(String[] args) {
        StreamsBuilder builder = new StreamsBuilder();
        KTable<String, StockPrice> stockPrices = builder.table("stock-prices");

        KTable<String, Long> stockUpdateCount = stockPrices.groupBy((stock, price) -> stock, Grouped.with(Serdes.String(), stockPriceSerde))
                                                              .count(Materialized.as("stock-update-count"));


        stockUpdateCount.toStream().foreach((stock, count) -> System.out.println("Stock: " + stock + " - Update Count: " + count));

        // start the stream
    }

    static class StockPrice {
        private String currentPrice;
        // removed index
        // getters and setters
    }
}
```

In this example, we're using the groupBy method on the stockPrices KTable to group the records by stock symbol, and then using the count method to create a new KTable called stockUpdateCount which counts the number of updates for each stock. The result is a new KTable where each record has the stock symbol as the key and the update count as the value.

Then we are printing the stock symbol and the count for each record in the stream.

Also, you can further aggregate the count on the basis of any key or value, for example on the basis of the current stock price, by using the aggregate method.

```
KTable<String, Long> stockUpdateCountByPrice = stockPrices.groupBy((stock, price) -> price.getCurrentPrice(), Grouped.with(Serdes.String(), stockPriceSerde))
                                                              .count(Materialized.as("stock-update-count-by-price"));
This would give you a Ktable where the key is the current stock price and the value is the number of updates for each stock price.
```