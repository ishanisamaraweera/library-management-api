# Library Management System

A Spring Boot based REST API for managing books and borrowers in a library system.
The application supports registering books and borrowers, borrowing and returning books, bulk operations, transactional consistency, containerization, CI, and multi environment configuration.

---

## Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database (dev and test)
- Maven
- Docker
- GitHub Actions
- JaCoCo

---

## Features

- Register borrowers and books
- Borrow and return books
- Enforces one borrower per book copy
- Supports multiple copies using same ISBN
- Uses H2 database for simplicity and portability
- Global exception handling
- Swagger UI
- Docker & Docker Compose
- H2 DB to save data
- Unit tests included with 92% test coverage, verified using JaCoCo

---

## Swagger

http://localhost:8080/swagger-ui.html

---

## Project Structure

```
library-management-api
 ├─ src
 │   ├─ main
 │   │   ├─ java
 │   │   └─ resources
 │   │       ├─ application.yml
 │   │       ├─ application-dev.yml
 │   │       ├─ application-test.yml
 │   │       └─ application-prod.yml
 │   └─ test
 ├─ .github
 │   └─ workflows
 │       └─ ci.yml
 ├─ Dockerfile
 ├─ pom.xml
 └─ README.md
```

---

## Running the Application

### Run locally with Maven

```bash
mvn spring-boot:run
```

or

### Run using packaged JAR

```bash
mvn clean package
set SPRING_PROFILES_ACTIVE=dev
java -jar target/library-management-api-1.0.0.jar

```

---

## Environment Configuration

The application supports multiple environments using Spring profiles.

### Available profiles

- dev
- test(qa)
- prod

Configuration is externalized using environment variables where required.

Example for production:

```bash
export DB_URL=jdbc:mysql://localhost:3306/library
export DB_USERNAME=library_user
export DB_PASSWORD=secret
SPRING_PROFILES_ACTIVE=prod java -jar app.jar
```

---

## API Endpoints

Base URL:

```
/library-management/api
```

### Register Borrower

POST `/borrowers`

```json
{
  "name": "Ishani",
  "email": "ishani@example.com"
}
```

### Register Book

POST `/books`

```json
{
  "isbn": "9780134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch"
}
```

### Bulk Register Books(Optional)

POST `/books/bulk`

### List All Books

GET `/books`

### Borrow Book

POST `/borrow/{bookId}/borrower/{borrowerId}`

### Return Book

POST `/return/{bookId}`

---

## Transaction Management

- Bulk book registration is transactional.
- Borrow and return operations are transactional.
- If any failure occurs during a transaction, changes are rolled back automatically.

---

## Assumptions

1. A book can be borrowed by only one borrower at a time.
2. A borrower can borrow multiple books simultaneously, provided each book is available.
3. ISBN uniqueness is assumed but not enforced at database level
4. Each book record supports a single author field. In cases where a book has multiple authors, all author names are stored as a comma-separated string.
5. Due dates, fines, reservations, and renewal policies are out of scope for this implementation.
6. Authentication and authorization are not implemented and are considered outside the scope of this task.
7. The application is stateless and exposes functionality exclusively via RESTful APIs.
8. An in-memory database is used for local development and testing, while an external database can be configured for production environments via Spring profiles.

---

## Containerization with Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/library-management-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
```

### Build and run

```bash
docker build -t library-management-api .
docker run -p 8080:8080 library-management-api
```

## CI Pipeline

Continuous Integration is implemented using GitHub Actions.

### Location

```
.github/workflows/ci.yml
```

### Pipeline steps

- Checkout code
- Setup JDK
- Build project
- Run tests
- Generate JaCoCo coverage report

---

## Test Coverage

- Unit tests for service layer
- Integration tests for controller endpoints
- JaCoCo used for coverage reporting
- Minimum coverage threshold enforced via Maven as 80%

---

## 12 Factor App Conformance

The application follows the 12 Factor App principles to a reasonable extent.

1. Codebase
   Single Git repository with one deployable application.

2. Dependencies
   All dependencies are explicitly declared in Maven pom.xml.

3. Config
   Configuration is externalized using Spring profiles and environment variables.

4. Backing Services
   Database is treated as an attached resource and configured externally.

5. Build, Release, Run
   Maven build, Docker image creation, and container execution are clearly separated.

6. Processes
   Application is stateless and runs as a REST API.

7. Port Binding
   Application is self contained and exposes HTTP via embedded Tomcat.

8. Concurrency
   Application can scale horizontally using container replicas.

9. Disposability
   Fast startup and graceful shutdown supported by Spring Boot.

Full adherence to all 12 factors is not required for this scope and is intentionally limited.

---

## Future Improvements

- Authentication and authorization
- Pagination and filtering
- Production database migration
- Kubernetes deployment
