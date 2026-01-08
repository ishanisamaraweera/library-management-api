
# Library REST API (Java 17)

## Features
- Java 17, Spring Boot 3
- RESTful API
- Global exception handling
- Swagger UI
- Docker & Docker Compose
- H2 DB (replaceable)
- Unit tests included

## Run locally
mvn clean package
java -jar target/library-api-1.0.0.jar

## Swagger
http://localhost:8080/swagger-ui.html

## Docker
docker-compose up --build

## 12 Factor Alignment
- Config via environment
- Stateless services
- Logs to stdout
- Disposable containers
- Dev/prod parity via Docker
