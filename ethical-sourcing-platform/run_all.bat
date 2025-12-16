@echo off
echo Starting Zipkin...
start "Zipkin" /B java -jar zipkin/zipkin.jar > logs/zipkin.log 2>&1
echo Zipkin started.

echo Waiting 5 seconds...
timeout /t 5 /nobreak >nul

echo Starting Discovery Service...
start "Discovery Service" /B java -jar discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar > logs/discovery.log 2>&1
echo Discovery Service started.

echo Waiting for Discovery Service to initialize (10s)...
timeout /t 10 /nobreak >nul

echo Starting Gateway Service...
start "Gateway Service" /B java -jar gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar > logs/gateway.log 2>&1
echo Gateway Service started.

echo Starting Supplier Service...
start "Supplier Service" /B java -jar supplier-service/target/supplier-service-0.0.1-SNAPSHOT.jar > logs/supplier.log 2>&1
echo Supplier Service started.

echo Starting Provenance Service...
start "Provenance Service" /B java -jar provenance-service/target/provenance-service-0.0.1-SNAPSHOT.jar > logs/provenance.log 2>&1
echo Provenance Service started.

echo Starting Audit Service...
start "Audit Service" /B java -jar audit-service/target/audit-service-0.0.1-SNAPSHOT.jar > logs/audit.log 2>&1
echo Audit Service started.

echo.
echo All services started in background. Logs are being written to logs/*.log files.
echo Run 'stop_all.bat' to stop all services.
pause
