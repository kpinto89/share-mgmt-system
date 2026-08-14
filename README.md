# Share Management System

A Spring Boot and Thymeleaf application for managing shareholders, recording buy/sell transactions, and tracking current holdings from a simple web UI.

## Tech stack

- Spring Boot 2.7
- Thymeleaf
- Spring Data JPA
- H2 in-memory database
- Gradle

## Applied design patterns

- **MVC:** controllers, templates, and form models separate the web flow.
- **Repository:** Spring Data repositories isolate persistence access.
- **Service Layer:** business orchestration and portfolio rules live in services.
- **Strategy + Factory:** buy and sell workflows use dedicated processors selected by transaction type.
- **Facade:** authentication state is exposed through a dedicated facade instead of leaking Spring Security details into controllers and templates.

## Features

- Dashboard with portfolio metrics and recent activity
- Shareholder registration screen
- Transaction ledger with buy/sell validation
- Portfolio summaries derived from recorded transactions
- Sample seed data for quick startup
- Login and logout with Spring Security

## Run locally

```bash
gradlew.bat bootRun
```

Open `http://localhost:8080` after startup.

## Default login

- Username: `admin`
- Password: `admin123`
