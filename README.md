
# Library REST API

Java 17 Spring Boot RESTful API for managing borrowers and books in a library system.

## Features
- Register borrowers and books
- Borrow and return books
- Enforces one borrower per book copy
- Supports multiple copies using same ISBN
- Uses H2 database for simplicity and portability

## Run
mvn spring-boot:run

## Endpoints
POST /api/borrowers
POST /api/books
GET /api/books
POST /api/borrow/{bookId}/borrower/{borrowerId}
POST /api/return/{bookId}
