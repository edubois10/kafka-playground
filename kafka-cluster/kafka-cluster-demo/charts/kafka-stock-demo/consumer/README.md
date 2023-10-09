## Generating the trustore.jks file

1. Copy the client-ca certificate generated by the strimzi operator in the namespace where the Kafka cluster is installed
``` shell
oc get secret dev-cluster-ca-cert -n $NAMESPACE -o=jsonpath='{.data.ca\.crt}' | base64 -d > kafka-client-ca.crt
```

2. Generate the trustore.jks
``` shell
keytool -import -trustcacerts -alias root \
 -file kafka-client-ca.crt -keystore truststore.jks \
 -storepass password -noprompt
```

3. Create a secret with the trustore.jks
``` shell
oc create secret generic demo-truststore --from-file=truststore.jks=./truststore.jks -n $NAMESPACE 
```

4. Create the helm chart
``` shell
helm template consumer-demo . -n demo | oc apply -n demo -f -
```