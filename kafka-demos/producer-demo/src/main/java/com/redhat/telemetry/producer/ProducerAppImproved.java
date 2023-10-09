package com.redhat.telemetry.producer;

import java.time.Duration;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import java.util.concurrent.TimeUnit;


@ApplicationScoped
public class ProducerAppImproved {

    private static final Logger LOG = Logger.getLogger(ProducerAppImproved.class);

    @Outgoing("stock-demo-1") 
    public Multi<Record<String, Double>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(500))  // Smallest duration to check every half second
                .onOverflow().drop()
                .flatMap(tick -> {
                    long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(tick * 500);
                    long segment = (elapsedMinutes % 9)/3;

                    Stock generator = new Sp500StockNameGenerator().generateStockName();
                    String stockName = generator.getName();
                    String currentStock = stockName;
                    
                    StockPriceGenerator priceGenerator = new StockPriceGenerator(generator);
                    double price = priceGenerator.generateNextPrice();
                    Double currentPrice = price;

                    if (segment % 3 == 0){

                        LOG.infov("Stock: {0}, Price: {1}", currentStock, currentPrice);
                        return Multi.createFrom().item(Record.of(currentStock, currentPrice));
                    }
                    else if (segment % 3 == 1){
                        if (tick % 8 == 0){
                            LOG.infov("Stock: {0}, Price: {1}", currentStock, currentPrice);
                            return Multi.createFrom().item(Record.of(currentStock, currentPrice));

                        }
                        return Multi.createFrom().empty(); 
                    }
                    else {
                        if (tick % 60 == 0){
                            LOG.infov("Stock: {0}, Price: {1}", currentStock, currentPrice);
                            return Multi.createFrom().item(Record.of(currentStock, currentPrice));
                        }
                        return Multi.createFrom().empty();
                    }
                });
    }
    /*
    private boolean shouldEmit(long elapsedMinutes, Duration currentDuration) {
        long segment = elapsedMinutes / 3;
        long minutesInSegment = elapsedMinutes % 3;
        long expectedTicksInSegment = minutesInSegment * 60 / currentDuration.getSeconds();
        
        return segment > expectedTicksInSegment;
    }

    private Duration getTickDuration(long elapsedMinutes) {
        long segment = (elapsedMinutes % 9) / 3;

        switch ((int) segment) {
            case 0:
                return Duration.ofMinutes(1);
            case 1:
                return Duration.ofSeconds(10);
            default:
                return Duration.ofSeconds(1);

        }
    }
    */
}
