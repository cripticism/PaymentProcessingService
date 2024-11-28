# PaymentProcessingService

## Overview
REST API for managing payment transactions in a bank account. This API allows listing, creating, updating and deleting of payments. The project is implemented in Java using Spring Boot and H2 as an in-memory database. Additionally, the service is containerized using Docker.

### 1. Basic REST Endpoints
- **GET /payments**: List all payments.
- **POST /payments**: Create a new payment.
- **PUT /payments**: Update a payment by ID.
- **DELETE /payments/{id}**: Delete a payment by ID.

### 2. Database
-  **H2** as the in-memory database.
- `payment` table with the following columns:
    - `id` (Long, primary key)
    - `amount` (Decimal)
    - `currency` (String)
    - `fromAccount` (String)
    - `toAccount` (String)
    - `timestamp` (Timestamp)
####
-  **Liquibase** as the database schema change management solution.

### 3. Implementation Details
- Implement basic REST endpoints (GET, POST, PUT, DELETE).
- Validate request payloads using Spring's built-in validation library.
- Add exception handling by creating a global exception handler using ControllerAdvice.
- Implement basic unit tests using JUnit and MockMvc.
- Implement pagination for the GET `/payments` endpoint.
- Add filtering capabilities for amount field.
- Integrate logging by using AOP.
- Ensure concurrency control by making the requests transactions.
- Implement transaction history, keeping a log of changes in payments.
- Secure the endpoints using Spring Security and JWT (JSON Web Token) to ensure only authenticated users can access/edit data.

### 4. Containerization
- Docker image for the application.
- Ensure the application can be run using a Docker container.

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Docker

### Running the Application Locally

1. **Clone the Repository**
   ```bash
   git clone https://gitlab.softvision.ro/andrei.clatinici/PaymentProcessingService.git
####
2. **Running the application inside Docker**
    ```bash
   mvn clean package
   docker build -t paymentprocessingservice:1.0.0 .
   docker run --name payments-container -p 8080:8080 -td paymentprocessingservice:1.0.0
   