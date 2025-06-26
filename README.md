# hobom-internal-backend

`hobom-internal-backend` is an internal backend service designed following the **Hexagonal Architecture** (Ports and Adapters and UseCases).  
It processes incoming messages from **Kafka**, handles **HoBom application logs**, and supports **email & push delivery** workflows.

## 🧱 Architecture

This service adheres to the principles of **Hexagonal Architecture**, separating domain logic from infrastructure concerns.

### Key Principles

- Domain-oriented, use-case driven
- Clean separation between core business logic and external technologies
- Strategy pattern for extensible message handling (e.g., email, push notifications)

---

## ⚙️ Features

- Stores delivery history and logs in PostgreSQL via **jOOQ**
- Designed for extension (e.g., SMS, push notifications in the future)

---

## 📦 Tech Stack

- **Spring Boot**
- **Kotlin**
- **jOOQ** for type-safe SQL
- **PostgreSQL**
- **Kafka** for message-driven architecture
- **Hexagonal Architecture** (clean separation by ports & adapters & use cases)
- **Gradle**
- **Spotless**
- **MockK**
- **JUnit 5**
