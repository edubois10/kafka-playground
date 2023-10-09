# Keda Demo

## Install Keda
``` shell
oc apply -f https://github.com/kedacore/keda/releases/download/v2.9.1/keda-2.9.1.yaml

oc patch deployment keda-operator -n keda --type=json -p='[{"op": "remove", "path": "/spec/template/spec/containers/0/securityContext/seccompProfile"}]'

oc patch deployment keda-metrics-apiserver -n keda --type=json -p='[{"op": "remove", "path": "/spec/template/spec/containers/0/securityContext/seccompProfile"}]'
```


## Creating SA
``` shell
oc create sa thanos -n keda
```

``` shell
TOKEN_SECRET=$(oc describe sa thanos -n keda | grep 'Tokens:' -A 1 | tail -n 1 | awk '{print $1}')
```

``` shell
sed "s/<SA_TOKEN>/${TOKEN_SECRET}/g" ./generic/cta-keda-raw.yaml > cta-keda.yaml
```

## Create the resources
``` shell
oc apply -f .
```
### Debug

Remove SCC from deployments
```
seccompProfile:
  type: RuntimeDefault
```
