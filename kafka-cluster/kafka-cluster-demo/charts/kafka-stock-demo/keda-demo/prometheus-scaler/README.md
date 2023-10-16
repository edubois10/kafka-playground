# Keda Demo

## Creating SA
``` shell
oc create sa thanos -n demo 
```

``` shell
TOKEN_SECRET=$(oc describe sa thanos -n demo | grep 'Tokens:' -A 1 | tail -n 1 | awk '{print $1}')
```

``` shell
sed "s/<SA_TOKEN>/${TOKEN_SECRET}/g" ./generic/cta-keda-raw.yaml > cta-keda.yaml
```

## Create the resources
``` shell
oc apply -f .
```