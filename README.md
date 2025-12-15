# Ethical Sourcing & Provenance Platform

## Executive Summary
Standard Supply Chain Management (SCM) systems often focus merely on quantity. This project elevates the concept to **Provenance and Ethical Sourcing**. The core abstract idea is to build a system that tracks the *lineage* of a product—from raw material extraction to the final consumer—ensuring compliance with ethical standards (e.g., fair labor, carbon footprint).

This domain addresses the "Abstract Idea" requirement by implementing a **Digital Ledger** pattern using Microservices. It requires strict data integrity, traceability, and cross-service communication, making it an ideal candidate for demonstrating the Spring Cloud ecosystem.

## Microservices Architecture

### Core Services
1.  **Supplier Identity Service (supplier-service):** Manages profiles of raw material providers. Handles strict validation (KYC) and assigns "Trust Scores".
2.  **Provenance Tracker Service (lineage-service):** The core "ledger". Records every hop in the supply chain using a graph-like data model.
3.  **Compliance & Audit Service (audit-service):** Asynchronous observer that listens for transaction events and logs immutable audit trails.

### Infrastructure Services
*   **Discovery Service:** Netflix Eureka Server.
*   **Gateway Service:** Spring Cloud Gateway.
*   **Observability:** Zipkin Server + Spring Cloud Sleuth.

## Getting Started

### Prerequisites
*   Java 17+
*   Maven
*   Docker & Docker Compose
*   Node.js (for frontend)

### Installation & Run

1.  **Clone the repository**
    ```bash
    git clone <repository-url>
    cd MS-SCM/ethical-sourcing-platform
    ```

2.  **Start Services with Docker Compose**
    ```bash
    docker-compose up -d --build
    ```

cd discovery-service && ./mvnw clean package -DskipTests
cd ../gateway-service && ./mvnw clean package -DskipTests
cd ../supplier-service && ./mvnw clean package -DskipTests
cd ../provenance-service && ./mvnw clean package -DskipTests
cd ../audit-service && ./mvnw clean package -DskipTests
cd ..




3.  **Access Services**
    *   Discovery Service (Eureka): `http://localhost:8761`
    *   Gateway: `http://localhost:8080`
    *   Zipkin: `http://localhost:9411`
    *   Swagger UI (Lineage Service): `http://localhost:8080/lineage-service/swagger-ui.html` (via Gateway)

### Frontend
Navigate to `ethical-sourcing-platform/frontend-app` and run:
```bash
npm install
npm run dev
```

## License
MIT

the testing of the project has benn underwhelming, we are just doing some curl commands, we should use some java testing frameworks to implement some robust testing mechanism