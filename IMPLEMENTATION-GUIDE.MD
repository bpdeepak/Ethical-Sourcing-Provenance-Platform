# **Implementation Guide: Ethical Sourcing & Provenance Platform**

## **1\. Environment & Version Strategy**

Before visiting Spring Initializer, you must decide on the version strategy. The syllabus explicitly mentions **Spring Cloud Sleuth**. However, Sleuth has been removed in Spring Boot 3.0+ and replaced by **Micrometer Tracing**.

* **Recommended Path:** Use **Spring Boot 3.3.x (Current Stable)**.  
  * *Why:* It is the industry standard.  
  * *Syllabus Adaptation:* You will fulfill the "Distributed Log Tracing" requirement using **Micrometer Tracing** (the modern equivalent of Sleuth). I have provided the specific dependencies below to make this work exactly like Sleuth.  
* **Java Version:** Java 17 (LTS) or Java 21 (LTS).

---

## **2\. Spring Initializer Setup (Step-by-Step)**

You need to generate **four** separate projects. Go to [start.spring.io](https://start.spring.io/) and generate the following artifacts.

### **Service 1: The Discovery Server (Eureka)**

This is the phonebook for your microservices.

* **Project:** Maven  
* **Spring Boot:** 3.3.x  
* **Group:** com.ethicalsource  
* **Artifact:** discovery-service  
* **Dependencies to Add:**  
  1. **Eureka Server** (spring-cloud-starter-netflix-eureka-server)  
  2. **Spring Boot Actuator** (For health checks)

### **Service 2: The API Gateway**

This is the single entry point that routes traffic to your services.

* **Artifact:** gateway-service  
* **Dependencies to Add:**  
  1. **Gateway** (spring-cloud-starter-gateway)  
  2. **Eureka Discovery Client** (spring-cloud-starter-netflix-eureka-client)  
  3. **Spring Boot Actuator**  
  4. **Lombok**

### **Service 3: Supplier Service (Core Data)**

Manages supplier identities and validation.

* **Artifact:** supplier-service  
* **Dependencies to Add:**  
  1. **Spring Web** (RESTful APIs)  
  2. **Spring Data JPA** (Database interaction)  
  3. **H2 Database** (For development simplicity)  
  4. **Eureka Discovery Client**  
  5. **Validation** (For @NotNull, custom annotations)  
  6. **Lombok** (Reduces boilerplate)  
  7. **Spring Boot Actuator**

### **Service 4: Provenance Service (The Ledger)**

Tracks the product lineage.

* **Artifact:** provenance-service  
* **Dependencies:** Same as **Supplier Service**, plus:  
  1. **OpenFeign** (spring-cloud-starter-openfeign) \- *Essential for communicating with the Supplier Service.*

---

## **3\. Critical Configuration: Distributed Tracing (Sleuth Replacement)**

**Attention:** start.spring.io does not have a simple "Sleuth" button anymore. To satisfy the syllabus requirement for "Distributed log tracing by using Zipkin and Sleuth" in Spring Boot 3, you must manually add the following dependencies to the pom.xml of **Gateway**, **Supplier**, and **Provenance** services after generating them.

**Open pom.xml for these 3 services and add this block:**

XML

\<dependency\>  
    \<groupId\>io.micrometer\</groupId\>  
    \<artifactId\>micrometer-tracing-bridge-brave\</artifactId\>  
\</dependency\>

\<dependency\>  
    \<groupId\>io.zipkin.reporter2\</groupId\>  
    \<artifactId\>zipkin-reporter-brave\</artifactId\>  
\</dependency\>

\<dependency\>  
    \<groupId\>io.github.openfeign\</groupId\>  
    \<artifactId\>feign-micrometer\</artifactId\>  
\</dependency\>

* **Concept Mapping:**  
  * **Old Syllabus:** Spring Cloud Sleuth $\\rightarrow$ **New Reality:** Micrometer Tracing Bridge Brave.  
  * **Old Syllabus:** Zipkin Client $\\rightarrow$ **New Reality:** Zipkin Reporter Brave.

---

## **4\. Implementation Phase 1: Directory Structure**

Organize your code into a root folder to simulate a mono-repo structure. This helps with managing the project in your IDE (IntelliJ/Eclipse).

ethical-sourcing-platform/

├── discovery-service/ \# Run on Port 8761

├── gateway-service/ \# Run on Port 8080

├── supplier-service/ \# Run on Port 8081

└── provenance-service/ \# Run on Port 8082

### **Immediate Next Steps for You**

1. **Download** the 4 zips from Spring Initializer.  
2. **Extract** them into the folder structure above.  
3. **Open** your IDE (IntelliJ IDEA recommended) and "Import Project" \-\> Select the root ethical-sourcing-platform folder (or open them as separate projects if preferred).  
4. **Verify** that java \-version in your terminal matches the Java version you selected (17 or 21).

Once you have the projects open, we can proceed to coding the **Eureka Server** configuration, which is the first requirement in your architecture syllabus.