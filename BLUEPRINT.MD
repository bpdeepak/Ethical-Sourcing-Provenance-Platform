# **Project Blueprint: Ethical Sourcing & Provenance Platform**

## **1\. Executive Summary & Abstract Domain**

Standard Supply Chain Management (SCM) systems often focus merely on quantity (e.g., "How many widgets do we have?"). This project elevates the concept to **Provenance and Ethical Sourcing**. The core abstract idea is to build a system that tracks the *lineage* of a product—from raw material extraction to the final consumer—ensuring compliance with ethical standards (e.g., fair labor, carbon footprint).

This domain addresses the "Abstract Idea" requirement by implementing a **Digital Ledger** pattern using Microservices. It requires strict data integrity, traceability, and cross-service communication, making it an ideal candidate for demonstrating the Spring Cloud ecosystem.

---

## **2\. Microservice Architecture (Syllabus Mapping)**

To satisfy the "minimum 3 services" requirement and the communication syllabus points, the system is composed of the following distinct deployable units.

### **2.1 Core Services**

1. **Supplier Identity Service (supplier-service):**  
   * **Responsibility:** Manages profiles of raw material providers. It handles strict validation (KYC \- Know Your Customer) and assigns "Trust Scores" based on audit data.  
   * **Database:** relational (PostgreSQL/MySQL) for structured profile data.  
2. **Provenance Tracker Service (lineage-service):**  
   * **Responsibility:** The core "ledger." It records every hop in the supply chain (e.g., Factory A $\\rightarrow$ Distributor B). It uses a graph-like data model to link AssetID to ParentAssetID.  
   * **Abstract Feature:** Implements a recursive "Trace Back" algorithm to verify the origin of a finished good.1  
3. **Compliance & Audit Service (audit-service):**  
   * **Responsibility:** An asynchronous observer that listens for transaction events. It runs checks (e.g., "Is the supplier certified?") and logs immutable audit trails.  
   * **Technical Highlight:** Heavily utilizes **Custom Annotations** for automatic logging.

### **2.2 Infrastructure Services**

* **Discovery Service:** Netflix Eureka Server.  
* **Gateway Service:** Spring Cloud Gateway (or Zuul).  
* **Observability:** Zipkin Server \+ Spring Cloud Sleuth (Micrometer Tracing).

---5173

## **3\. Technical Implementation Guide**

### **3.1 Getting Started & Environment**

* **Spring Initializr Config:**  
  * Dependencies: Spring Web, Spring Data JPA, H2/MySQL Driver, Eureka Discovery Client, Config Client, Lombok.  
  * **Build Tool:** Maven (Multi-module project recommended for shared DTOs).

### **3.2 Communication & Discovery (REST \+ Eureka)**

**Syllabus:** *Communication between 2 or more microservices / Setting up Eureka.*

* **Service Registry (Eureka):**  
  * All three services (supplier, lineage, audit) must annotate their main class with @EnableDiscoveryClient.  
  * application.yml configuration:  
    YAML  
    eureka:  
      client:  
        serviceUrl:  
          defaultZone: http://localhost:8761/eureka/

* **Inter-service Communication (Feign):**  
  * The **Provenance Tracker** needs to validate a supplier before recording a shipment.  
  * Instead of hardcoding URLs, it uses OpenFeign:  
    Java  
    @FeignClient(name \= "SUPPLIER-SERVICE")  
    public interface SupplierClient {  
        @GetMapping("/api/suppliers/{id}/trust-score")  
        TrustScoreDTO getTrustScore(@PathVariable("id") String id);  
    }

  * This satisfies the requirement for "Communication between 2 or more microservices" using abstract service names resolved by Eureka.2

### **3.3 Distributed Log Tracing (Zipkin & Sleuth)**

**Syllabus:** *Distributed log tracing by using zipkin and spring cloud sleuth.*

In a supply chain, if a compliance check fails, you must know exactly *where* it failed.

* **Implementation:** Add spring-cloud-starter-sleuth and spring-cloud-starter-zipkin dependencies.  
* **The Flow:**  
  1. User sends a "Register Shipment" request to the Gateway.  
  2. Gateway forwards to lineage-service.  
  3. lineage-service calls supplier-service via Feign.  
* **Visualization:** Sleuth adds a TraceID to the HTTP headers. Zipkin aggregates these. You can view a Waterfall graph showing the latency of the database query in supplier-service relative to the initial request.2  
* *Note for Spring Boot 3:* If you use the latest Spring Boot 3, "Sleuth" has been replaced by **Micrometer Tracing**, but the concept and Zipkin integration remain the primary syllabus target.

### **3.4 Connecting to Database**

**Syllabus:** *Connecting spring microservices with a data base.*

* Domain Model (Lineage):  
  The lineage-service requires a recursive relationship.  
  Java  
  @Entity  
  public class ProductEvent {  
      @Id @GeneratedValue  
      private Long id;  
      private String assetId;  
      private String parentAssetId; // Links to the previous form of the material  
      private String location;  
      private LocalDateTime timestamp;  
  }

* **Multi-Tenancy (Abstract Idea):** To make this "distinct," implement a **Schema-per-Tenant** approach. The database connection adjusts dynamically based on the X-Tenant-ID header, allowing multiple logistic companies to use your platform securely.4

### **3.5 Custom Annotations**

**Syllabus:** *Creating custom annotations (at method level and class level).*

This is where you demonstrate advanced engineering capability.

**Idea 1: Method Level \- @EthicalCheck**

* **Purpose:** Automatically verifies if a supplier is "Red Flagged" before processing any transaction.  
* **Implementation:** AOP (Aspect Oriented Programming).  
  Java  
  @Target(ElementType.METHOD)  
  @Retention(RetentionPolicy.RUNTIME)  
  public @interface EthicalCheck {  
      String severity() default "HIGH";  
  }

* **Aspect Logic:** The Aspect intercepts the method, extracts the supplierId from the arguments, checks a blacklist cache, and throws a ComplianceException if the supplier is banned. This keeps your business logic clean.

**Idea 2: Class Level \- @AuditableDomain**

* **Purpose:** Flags an entire Entity class to be tracked by the audit-service. Any save or update on this entity triggers an asynchronous event to the audit log.6

### **3.6 Mocking and Testing**

**Syllabus:** *Mocking and Testing service by using Mocikto and Junit.*

* **Scenario:** Testing the ProvenanceService.recordTransfer() method.  
* **The Problem:** This method calls the SupplierClient (external service). You cannot rely on the external service being up during unit tests.  
* **Mockito Solution:**  
  Java  
  @MockBean  
  private SupplierClient supplierClient;

  @Test  
  public void shouldDenyShipmentFromBannedSupplier() {  
      // Mock the external behavior  
      given(supplierClient.getTrustScore("SUP-999"))  
          .willReturn(new TrustScoreDTO(0, "BANNED"));

      // Assert business logic handles it  
      assertThrows(ComplianceException.class, () \-\> {  
           service.recordTransfer("SUP-999", "Item-1");  
      });  
  }

* **JUnit Integration:** Use @SpringBootTest for integration tests that spin up an H2 in-memory database to verify the JPA mappings.

### **3.7 Deployment (Docker & Kubernetes)**

**Syllabus:** *Deploying microservices using Docker/Kubernetes.*

1. **Dockerfile:** Create a multi-stage Dockerfile for each service to minimize image size (using eclipse-temurin:17-jre-alpine).  
2. **Docker Compose:** Use docker-compose.yml to orchestrate the 3 services \+ Zipkin \+ Database locally for development.  
3. **Kubernetes (K8s):**  
   * Create deployment.yaml and service.yaml for the lineage-service.  
   * Use **Environment Variables** in K8s to inject the Eureka URL:  
     YAML  
     env:  
       \- name: EUREKA\_CLIENT\_SERVICEURL\_DEFAULTZONE  
         value: "http://discovery-service:8761/eureka/"

---

## **4\. Swagger UI Integration**

**Syllabus:** *Setting up Swagger ui.*

* Add springdoc-openapi-starter-webmvc-ui dependency.  
* The lineage-service will automatically generate interactive documentation at /swagger-ui.html.  
* **Benefit:** Allows you to demonstrate the API visually to professors or stakeholders without writing a frontend.7

#### **Works cited**

1. Product lineage \- AWS Supply Chain, accessed November 24, 2025, [https://docs.aws.amazon.com/aws-supply-chain/latest/userguide/product\_lineage.html](https://docs.aws.amazon.com/aws-supply-chain/latest/userguide/product_lineage.html)  
2. Tracing Request in Distributed Microservice Architecture using Spring Cloud Sleuth | by Rohit Malhotra | Medium, accessed November 24, 2025, [https://medium.com/@rohit.malhotra\_86/distributed-tracing-with-spring-sleuth-and-elk-stack-part-1-of-3-f761b034b7c3](https://medium.com/@rohit.malhotra_86/distributed-tracing-with-spring-sleuth-and-elk-stack-part-1-of-3-f761b034b7c3)  
3. Logging in Microservices: 5 Best Practices \- Better Stack, accessed November 24, 2025, [https://betterstack.com/community/guides/logging/logging-microservices/](https://betterstack.com/community/guides/logging/logging-microservices/)  
4. Building Secure Multi-Tenant Applications with Spring Boot: A Complete Implementation Guide | by Harsh Shah | Medium, accessed November 24, 2025, [https://medium.com/@shahharsh172/building-secure-multi-tenant-applications-with-spring-boot-a-complete-implementation-guide-3e5857bc7f7f](https://medium.com/@shahharsh172/building-secure-multi-tenant-applications-with-spring-boot-a-complete-implementation-guide-3e5857bc7f7f)  
5. How to implement a Multi Tenant Spring Boot application ( where each user has its own Database) \- Stack Overflow, accessed November 24, 2025, [https://stackoverflow.com/questions/64945839/how-to-implement-a-multi-tenant-spring-boot-application-where-each-user-has-it](https://stackoverflow.com/questions/64945839/how-to-implement-a-multi-tenant-spring-boot-application-where-each-user-has-it)  
6. Spring Boot REST API: Implementing Custom Audit Logging \- StackHawk, accessed November 24, 2025, [https://www.stackhawk.com/blog/creating-an-audit-trail-for-spring-controllers/](https://www.stackhawk.com/blog/creating-an-audit-trail-for-spring-controllers/)  
7. Monitoring Spring Boot Microservices with Actuator, Micrometer, and OpenTelemetry, accessed November 24, 2025, [https://uptrace.dev/blog/spring-boot-microservices-monitoring](https://uptrace.dev/blog/spring-boot-microservices-monitoring)