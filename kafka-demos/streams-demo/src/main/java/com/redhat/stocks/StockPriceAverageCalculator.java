package com.redhat.stocks;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.common.utils.Bytes;

import java.time.Instant;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;


@ApplicationScoped
public class StockPriceAverageCalculator {

    // Deserializer for NULL keys.
    private final Serde<String> keySerde = Serdes.String();
    private final Serde<Double> valueSerde = Serdes.Double();    

    @Produces
    public Topology buildTopology() {
        
        // Create a Kafka Streams builder
        StreamsBuilder builder = new StreamsBuilder();
        
        ObjectMapperSerde<StockPriceAverage> stockPriceSerde = new ObjectMapperSerde<>(StockPriceAverage.class);
        
        // Create a KStream that consumes records from the "stockPrice" topic
        KStream<String, Double> stockPriceStream = builder.stream("stock-demo-1", Consumed.with(keySerde, valueSerde));

        // TODO: print stream values
        //stockPriceStream.foreach((key, value) -> System.out.println("Stock: "+ key + " Value: " + value));

        KTable<String, Double> stockPriceTable = stockPriceStream
        .groupByKey()
        .aggregate(() -> new StockPriceAverage(0.0, 0),
                (key, value, aggregate) -> aggregate.addValue(value),
                Materialized.<String, StockPriceAverage, KeyValueStore<Bytes, byte[]>>as("stock-average-store")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(stockPriceSerde))
        .mapValues(StockPriceAverage::getAverageValue);
        
        stockPriceTable.toStream().foreach((key, value) ->
            System.out.println(
                "The current average for: " + key + " is: " + value  + "\n"
            )
        );
     
        return builder.build();
        
    }
}