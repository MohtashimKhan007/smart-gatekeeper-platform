# Smart GateKeeper Platform 🛡️

[![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-brightgreen.svg?style=flat&logo=springboot)](https://spring.io/projects/spring-boot)
[![Security](https://img.shields.io/badge/Spring%20Security-JWT%20RBAC-blue.svg?style=flat&logo=springsecurity)](https://spring.io/projects/spring-security)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-Event%20Driven-black.svg?style=flat&logo=apachekafka)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg?style=flat&logo=docker)](https://www.docker.com/)

A modern, cloud-native **Multi-Tenant API Gateway Control Plane** engineered in Spring Boot 3 & Java 21. GateKeeper provides multi-organization isolation, dynamic routing rules with path patterns and header filtering, role-based access control (RBAC), and distributed event synchronization via Apache Kafka.

---

## 🚀 Live Demo & Links

- **Live Application URL**: [Interactive Control Plane](https://smart-gatekeeper-platform.onrender.com)
- **Interactive Swagger Documentation**: [Swagger UI Explorer](https://smart-gatekeeper-platform.onrender.com/swagger-ui/index.html)
- **OpenAPI 3 Specification**: [OpenAPI JSON Spec](https://smart-gatekeeper-platform.onrender.com/v3/api-docs)

---

## 🌟 Key Architecture & Capabilities

```
+----------------------------------------------------------------------------------------+
|                                    GATEKEEPER PLATFORM                                 |
+----------------------------------------------------------------------------------------+
                                         │
        ┌────────────────────────────────┼────────────────────────────────┐
        ▼                                ▼                                ▼
[ API Management Service ]       [ Smart Gateway Service ]       [ Analytics Service ]
 (Control Plane / Auth / DB)       (Data Plane Routing)          (Telemetry & Observability)
        │                                ▲                                │
        │──────> [ Apache Kafka ] ───────│                                │
        │        (Config Events)                                          │
        ▼                                                                 ▼
[ MySQL / PostgreSQL ]                                             [ Storage Store ]
```

### 1. Multi-Tenant Architecture & Boundary Isolation
- **Organization Management**: Dynamic provisioning of organizations with isolated tenant scopes.
- **Role-Based Access Control (RBAC)**: Enforced via Spring Security and stateless JWT bearer tokens (`PLATFORM_ADMIN`, `TENANT_ADMIN`, `GATEWAY_SERVICE`).

### 2. Dynamic Route Versioning & Traffic Control
- Create and activate routes with custom path patterns, downstream targets, rate limits (requests per minute), response timeouts, and header routing rules.
- Real-time route state versioning: Incrementally tracked so API gateways immediately synchronize with the latest state.

### 3. Distributed Configuration Sync
- Publishes configuration change events to **Apache Kafka** using `@TransactionalEventListener(phase = AFTER_COMMIT)` ensuring zero phantom events on database rollbacks.

### 4. Cloud Native & Resilient
- **Dynamic URI parsing**: Converts cloud provider database URLs (`postgres://` / `mysql://`) into valid JDBC connection URLs on boot.
- Containerized using multi-stage lightweight Alpine JRE images with tiered JIT compilation flags.

---

## 🔐 Demo Credentials (Seeded)

| Role | Username | Password | Access Scope |
| :--- | :--- | :--- | :--- |
| **Platform Administrator** | `platform-admin` | `platform123` | Full control plane (Organizations, Global audits, Tenant admins) |
| **Tenant Administrator** | `foodfast-admin` | `foodfast123` | Organization scope (Manage routes, policies, header rules) |
| **Tenant Administrator** | `quickshop-admin` | `quickshop123` | Organization scope (Manage routes, policies, header rules) |
| **Gateway Internal** | `gateway-service` | `gateway123` | Internal data plane sync |

---

## 🛠️ Tech Stack

- **Language & Runtime**: Java 21 (LTS)
- **Framework**: Spring Boot 3.4+, Spring Data JPA, Spring Security, Spring WebMVC
- **Documentation**: SpringDoc OpenAPI 3 / Swagger UI
- **Messaging**: Apache Kafka / Spring Kafka
- **Database & Migration**: MySQL 8.0, Flyway Database Migrations
- **Containerization**: Docker Multi-stage Alpine builds
- **Authentication**: Nimbus OAuth2 JWT / BCrypt Password Encryption

---

## 🏃 Running Locally

### Prerequisites
- JDK 21+
- MySQL 8.0+ or Docker Compose

### 1. Start Infrastructure
```bash
docker compose -f code/gatekeeper/compose.yaml up -d
```

### 2. Run API Management Service
```bash
cd code/gatekeeper/api-management-service
./mvnw spring-boot:run
```
Access the application dashboard at `http://localhost:8081` and Swagger UI at `http://localhost:8081/swagger-ui/index.html`.

---

## 🧪 Testing

```bash
cd code/gatekeeper/api-management-service
./mvnw clean test
```

---

## 👨‍💻 Author

**Mohtashim Khan**
- GitHub: [@MohtashimKhan007](https://github.com/MohtashimKhan007)
- Email: manazkhanjhn786@gmail.com
