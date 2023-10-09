# kafka-demo
## Create trustsore from certificate
```
keytool -import -trustcacerts -alias root  -file kafka-cluster.crt -keystore truststore.jks  -storepass password -noprompt
```

## Kafka CLI
```
# Consume message from topic
./kafka-console-consumer.sh --bootstrap-server=my-cluster-kafka-brokers:9092 --topic stock-demo-1 --partition 2 --value-deserializer="org.apache.kafka.common.serialization.DoubleDeserializer" --key-deserializer="org.apache.kafka.common.serialization.StringDeserializer" --property key=value --from-beginning
```