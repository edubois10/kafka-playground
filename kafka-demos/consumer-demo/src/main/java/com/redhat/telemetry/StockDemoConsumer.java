package com.redhat.telemetry;

import javax.inject.Singleton;
//import javax.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;




import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
//import io.smallrye.reactive.messaging.annotations.Acknowledgment;

/* 
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.ConvertibleBaseBarBuilder;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.DoubleNum;
*/

@Singleton
public class StockDemoConsumer {

    
    @Incoming("stockdemo")
    public void consume(double stockValue) {
        System.out.println("Received stock value: " + stockValue);
    }
    
    /*
    @Incoming("stockdemo")
    public CompletionStage<Void> consume(IncomingKafkaRecord<String, Double> message) {
        // We don't need to ACK messages because in this example,
        // we set offset during consumer rebalance
        String key = message.getKey();
        Double value = message.getPayload();
        System.out.println("Received message with key: " + key + " and value: " + value);
        return CompletableFuture.completedFuture(null);
    }
    */

}
