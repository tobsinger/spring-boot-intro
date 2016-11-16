# spring-boot-intro

This project aims to help get started with spring boot.

## Features
- Embedded H2 Development-Database
- Query data using JPA with QueryDSL
- Serialize results as JSON

## Required
- Java 1.8
- Gradle
- IntelliJ IDEA

## Startup
### Terminal
> $ gradle bootRun

### Intellij
Gradle > Tasks > application > bootRun

## Interface documtentation and testing
http://localhost:8080/swagger-ui.html

## Endpoints
> GET: http://localhost:8080/customer/id/{ID}

> GET: http://localhost:8080/customer/lasname/{NAME}

> PUT: http://localhost:8080/customer
