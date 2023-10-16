# Keda Kafka Scaler Demo

## Keda Scaler Workflow:
### Configuration
A ***ScaledObject*** is configured, specifying Kafka as the trigger, defining relevant metrics (e.g., lag threshold), and pointing to the target deployment that needs to be scaled.
If authentication is needed, a TriggerAuthentication CRD is configured to securely manage credentials.
``` yaml
apiVersion: keda.sh/v1alpha1
kind: ScaledObject
metadata:
  ...
spec:
  scaleTargetRef:
    ...
  triggers:
    - authenticationRef:
        kind: TriggerAuthentication
        name: keda-trigger-auth-kafka-credential
      metadata:
        bootstrapServers: >-
          dev-kafka-bootstrap-kafka.apps.cluster-lwp66.lwp66.sandbox1482.opentlc.com:443
        consumerGroup: stock-demo-1
        lagThreshold: '5'
        topic: stock-demo-1
      type: kafka
```

### Metrics Collection:

1. The Metrics Server communicates with Kafka, querying the consumer group's lag (difference between the last produced message and last committed offset) in the specified topic.

2. The Metrics Server translates the raw Kafka metrics into metrics understandable by OpenShift.

### Scaling Evaluation:

1. The controller observes the ScaledObject and queries the Metrics Server for the actual metric value.
2. It then compares the Kafka consumer group lag with the predefined threshold in the ScaledObject definition.

### Decision Making:

1. If the lag exceeds the defined limit (indicating processing is falling behind and more processing power is needed), a scaling event is triggered.
If below or equal, and additional pods are running, it might scale down to minimize resource usage.


### Scaling Action:

#### Activation Phase (with Custom Metrics Autoscaler):

The custom metrics autoscaler has the capability to scale to zero if minReplicaCount in the custom metrics autoscaler CR is set to 0.
This phase encompasses scaling from 0 to 1 replicas and vice versa. When scaling up, after reaching 1 replica, the custom metrics autoscaler hands off control to the HPA.

#### Scaling Phase (Managed by HPA):

HPA queries the external metrics from KEDA and evaluates them against the scaling criteria defined in the ScaledObject.
HPA interacts with the Kubernetes API to scale the deployment. The deployment's replica count is adjusted: if the lag was high, it is increased; if the processing caught up and additional pods are unnecessary, it might be decreased.

### Pod Adjustment:

The ReplicaSet notices the change in the desired replica count and creates or terminates pods accordingly.
In the scale-out scenario (lag was high), new pods are spun up and registered as Kafka consumers, helping to process messages and reduce the lag.

### Processing and Feedback:

As new pods start processing messages, the consumer group lag should start to reduce.
KEDA continues to monitor the lag: if it goes below the threshold consistently, unnecessary pods might be terminated to conserve resources.
Continuous Monitoring:




