#!/bin/bash

kubectl create -f config-server-deployment.json
kubectl create -f config-server-svc.json
sleep 100s

kubectl create -f api-gateway-deployment.json
kubectl create -f api-gateway-svc.json

sleep 20s

kubectl create -f item-service-deployment.json
kubectl create -f item-service-svc.json

sleep 20s

kubectl create -f order-service-deployment.json
kubectl create -f order-service-svc.json

sleep 20s

kubectl create -f ui-deployment.json
kubectl create -f ui-svc.json

sleep 20s

kubectl create -f user-service-deployment.json
kubectl create -f user-service-svc.json

echo "Group 4's car shop will soon be ready"