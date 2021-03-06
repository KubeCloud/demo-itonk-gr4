#!/bin/bash

echo "Group 4's car shop is starting up"
echo
echo "Setting context to custer11"
kubectl config set-cluster cluster11 --server=http://192.168.1.11:8080
kubectl config set-context cluster11 --cluster=cluster11
kubectl config use-context cluster11


echo "Creating deployments and services"
kubectl create -f kubernetes/config-server-deployment.json
kubectl create -f kubernetes/config-server-svc.json
sleep 100s

kubectl create -f kubernetes/api-gateway-deployment.json
kubectl create -f kubernetes/api-gateway-svc.json

sleep 20s

kubectl create -f kubernetes/item-service-deployment.json
kubectl create -f kubernetes/item-service-svc.json

sleep 20s

kubectl create -f kubernetes/order-service-deployment.json
kubectl create -f kubernetes/order-service-svc.json

sleep 20s

kubectl create -f kubernetes/ui-deployment.json
kubectl create -f kubernetes/ui-svc.json

sleep 20s

kubectl create -f kubernetes/user-service-deployment.json
kubectl create -f kubernetes/user-service-svc.json

echo "Group 4's car shop will soon be ready"