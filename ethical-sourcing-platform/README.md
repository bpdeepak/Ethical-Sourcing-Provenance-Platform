# Ethical Sourcing Platform

A microservices-based platform for ethical sourcing and supply chain provenance.

## Prerequisites
- Java 17+
- Maven 3.8+
- Docker (optional, if running PostgreSQL in container)

## Running the Application

### Linux / macOS
1. **Start Services**:
   ```bash
   ./run_all.sh
   ```
   This will start Zipkin, Discovery Code, Gateway, and all microservices in the background. Logs are written to the `logs/` directory.

2. **Verify Installation**:
   ```bash
   ./verify_phase5.sh
   ```

3. **Stop Services**:
   ```bash
   pkill -f java
   ```

### Windows
1. **Start Services**:
   Double-click `run_all.bat` or run it from the command prompt:
   ```cmd
   run_all.bat
   ```
   Do not close the command window if you want to keep the logs visible, although they are redirected to `logs/`.

2. **Verify Installation**:
   Run the PowerShell verification script:
   ```powershell
   .\verify_phase5.ps1
   ```

3. **Stop Services**:
   Double-click `stop_all.bat` or run:
   ```cmd
   stop_all.bat
   ```
   **Note**: This kills all Java processes on the machine.

## Accessing Services
- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **Zipkin UI**: http://localhost:9411
