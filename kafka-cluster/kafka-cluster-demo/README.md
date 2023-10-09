# Kafka Cluster Demo 

## Configure OpenShift and deploy Helm charts

## Login into OpenShit with `oc`

### Create Projects

Create the demo `Project`

```shell
oc new-project $NAMESPACE
```

### Setup Cluster

Enable monitoring for user defined projects, like described [here](https://docs.openshift.com/container-platform/4.13/monitoring/enabling-monitoring-for-user-defined-projects.html)
```shell
helm template $STAGE charts/setup-cluster-monitoring/ -n openshift-monitoring | oc apply -n openshift-monitoring -f -
```

### Setup Namespace

Install all required Operators into the `kafka` `Namespace`
```shell
helm template $STAGE charts/setup-namespace/ -n $NAMESPACE | oc apply -n $NAMESPACE -f -
```

### Install AMQStreams `Kafka` custom resource

Install `Kafka` custom resources
```shell
helm template $STAGE charts/amqstreams-kafka/ -n $NAMESPACE | oc apply -n $NAMESPACE -f -
```

### Install Cluster User Workload Monitoring

Install `User Workload Monitoring` configmap
```shell
helm template $STAGE charts/cluster-user-workload-monitoring/ | oc apply -f -
```

### Before Setting the amqstreams monitoring, follow the README file in the chart

Install `PodMonitor` custom resources
```shell
helm template $STAGE charts/amqstreams-monitoring/ -n $NAMESPACE | oc apply -n $NAMESPACE -f -
```

The Grafana deployment will fail because of a SCC. Make sure to remove the following from the deployment
```
securityContext:
  seccompProfile:
    type: RuntimeDefault
```