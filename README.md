# TodoForge Microservices

TodoForge is a microservices-based todo application that demonstrates the implementation of a scalable, distributed system. The architecture is built using Java and Spring Boot, leveraging various Spring Cloud technologies to provide essential microservice patterns such as service discovery, API gateway, configuration management, distributed tracing, and observability.

## Features
- **Microservices Architecture**: Each service is independently deployable and scalable.
- **Authentication and Authorization**: Managed by `auth-service` using OAuth2.
- **Account Management**: Handled by `account-service`.
- **Task Management**: Core todo functionality managed by `todo-service`.
- **Centralized Configuration**: Configurations are managed by `config-service` using Spring Cloud Config.
- **Service Discovery**: `discovery-service` enables automatic service registration and discovery using Spring Cloud Netflix Eureka.
- **API Gateway**: All traffic is routed through `gateway-service` using Spring Cloud Gateway.
- **Tracing**: Distributed tracing with Zipkin and Spring Cloud Sleuth.
- **Monitoring**: Prometheus and Grafana for metrics collection and visualization.
- **Centralized Logging**: ELK (Elasticsearch, Logstash, and Kibana) stack for centralized logging and analytics.

## Technology Stack

- **Java**: The programming language used across all services.
- **Spring Boot**: Simplifies the development of microservices.
- **Spring Data JPA**: Provides a way to persist data.
- **Spring Security OAuth2**: Manages authentication and authorization.
- **Spring Cloud**: 
  - **Spring Cloud Config**: Centralized external configuration management.
  - **Spring Cloud Gateway**: Provides a flexible routing mechanism.
  - **Spring Cloud Netflix Eureka**: Service registry and discovery.
  - **Spring Cloud Sleuth**: Distributed tracing.
  - **Spring Cloud Hystrix**: handles latency and fault tolerance (Circuit Breaker pattern).
- **Feign Client**: Simplifies inter-service communication.
- **Zipkin**: Distributed tracing system to troubleshoot latency problems.
- **ELK Stack**: For centralized logging (Elasticsearch, Logstash, Kibana).
- **Prometheus & Grafana**: Monitoring and visualization of metrics.
- **Docker & Docker Compose**: Containerization of services and simplified orchestration.

## Microservices

1. **auth-service**: Manages user authentication and authorization using OAuth2.
2. **account-service**: Manages user accounts and profiles.
3. **todo-service**: Provides functionality to create, read, update, and delete todos.
4. **config-service**: Centralized configuration management using Spring Cloud Config.
5. **discovery-service**: Service registry and discovery using Eureka.
6. **gateway-service**: API gateway for routing requests to the respective microservices.

## Prerequisites

- **Java 8-11**
- **Docker** and **Docker Compose**
- **Maven** for build automation

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/thaiDev1997/todo-forge-microservices.git
2. Change directory:
   ```bash
   cd todo-forge-microservices
3. Build the project:
   ```bash
   mvn clean install
4. Start the services using Docker Compose
   ```bash
   docker-compose --file docker-compose.yml up --build
5. Access the services
 <br />**API Gateway**: http://localhost:8082
 <br />**Authorization**: http://localhost:8083
 <br />**Zipkin Dashboard**: http://localhost:9411
 <br />**Kibana Dashboard**: http://localhost:5601
 <br />**Prometheus**: http://localhost:9090
 <br />**Grafana**: http://localhost:3000 (username: admin - password: admin)

## Authorization
**[oauthdebugger](https://oauthdebugger.com/)**
<br />![image](https://github.com/user-attachments/assets/7e99a89d-b9da-4c4b-ab95-52d0c87eb3d4)
- **Account**:
    - **username**: admin
    - **password**: test123

## Monitoring and Observability
- **Tracing**: Distributed tracing is enabled via Zipkin. All requests are traced and you can view the trace logs in the Zipkin dashboard.
- **Logging**: Centralized logging is configured using the ELK stack. Logs from all services are collected and can be analyzed in the Kibana dashboard.
- **Metrics**: Prometheus is used for collecting metrics, and Grafana is used for visualizing them.

## Configuration
All services fetch their configurations from the **config-service**. You can update configuration files stored in the **config-service** repository and refresh the services without redeploying them by **@RefreshScope**.

## Contributors
[thaiDev1997](https://github.com/thaiDev1997)
