# Steps to setup Grafana Datasource

## Grafana `ServiceAccount` configuration

With the next command, we will save the token for the service account which will be used for accessing the grafana service. This will enable us to authenticate to the Prometheus service using the service account's token.

```shell
GRAFANA_SA_TOKEN=$(oc sa new-token $STAGE-grafana-sa -n $NAMESPACE)
```

```shell
echo $GRAFANA_SA_TOKEN
```

To replace the environment variable $GRAFANA_SA_TOKEN in the file cr-grafana-datasource.yaml with its corresponding value, please run the following GNU sed command:

```shell
sed "s|<GRAFANA_SA_TOKEN>|$GRAFANA_SA_TOKEN|g" ./files/cr-grafanadatasource.yaml > ./templates/cr-grafana-datasource.yaml
```

Please make sure that the environment variable $GRAFANA_SA_TOKEN has been correctly replaced in the yaml file before running the next command.

Use the following command to apply the modified datasource cr yaml file with the oc apply command:

```shell
oc apply -f cr-grafana-datasource.yaml
```