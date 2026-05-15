@echo off

start "Helm Deploy" cmd /k "helm install grafana ./grafana && helm install alloy ./alloy && helm install loki ./loki && helm install tempo ./tempo && helm install kafka ./kafka && helm install keycloak ./keycloak && helm install kube-prometheus ./kube-prometheus"