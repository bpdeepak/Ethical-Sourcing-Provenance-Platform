#!/bin/bash

echo "Starting Zipkin..."
java -jar zipkin/zipkin.jar > logs/zipkin.log 2>&1 &
echo "Zipkin started (PID: $!)"
sleep 5

echo "Starting PostgreSQL (Docker)..."
docker compose up -d postgres
echo "Waiting for PostgreSQL to be ready (10s)..."
sleep 10

echo "Starting Discovery Service..."
nohup java -jar discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar > logs/discovery.log 2>&1 &
echo "Discovery Service started (PID: $!)"

echo "Waiting for Discovery Service to initialize (10s)..."
sleep 10

echo "Starting Gateway Service..."
nohup java -jar gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar > logs/gateway.log 2>&1 &
echo "Gateway Service started (PID: $!)"

echo "Starting Supplier Service..."
nohup java -jar supplier-service/target/supplier-service-0.0.1-SNAPSHOT.jar > logs/supplier.log 2>&1 &
echo "Supplier Service started (PID: $!)"

echo "Starting Provenance Service..."
nohup java -jar provenance-service/target/provenance-service-0.0.1-SNAPSHOT.jar > logs/provenance.log 2>&1 &
echo "Provenance Service started (PID: $!)"

echo "Starting Audit Service..."
nohup java -jar audit-service/target/audit-service-0.0.1-SNAPSHOT.jar > logs/audit.log 2>&1 &
echo "Audit Service started (PID: $!)"

echo "All services started in background. Logs are being written to logs/*.log files."
echo "Run 'pkill -f java' to stop all services."
