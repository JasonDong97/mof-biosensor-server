# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MiQroBreath Backend - Spring Boot 3.2.3 backend service for a smart breath detection device (智能呼气检测仪). Provides device management, detection data recording, and health analysis services for a WeChat mini-program.

**Tech Stack**: Java 17+, Spring Boot 3.2.3, MyBatis Plus 3.5.15, MySQL 8.0+, Redis 6.0+, Sa-Token 1.45.0 (auth), Knife4j 4.4.0 (API docs)

## Build & Run Commands

```bash
# Compile
mvn compile -q

# Package (skip tests)
mvn clean package -DskipTests

# Run locally
mvn spring-boot:run

# Run JAR directly
java -jar target/miqrobreath-backend-0.0.1.jar

# Run tests
mvn test

# Code style check
mvn checkstyle:check
```

**Default server**: http://localhost:9093/api | API docs: http://localhost:9093/api/doc.html

## Architecture

```
src/main/java/com/miqroera/biosensor/
├── domain/          # Domain layer
│   ├── mapper/      # MyBatis Mapper interfaces
│   ├── model/       # Entity, DTO, VO classes
│   └── service/     # Business logic (interfaces + impl/)
├── infra/           # Infrastructure layer
│   ├── config/      # Spring configurations
│   ├── domain/      # R<T> response, PageResult, exceptions
│   └── util/        # Utilities (RedisUtil, ServletUtils, etc.)
└── web/             # Controller layer
    ├── AuthController.java
    ├── DeviceController.java
    ├── RecordController.java
    ├── FeedbackController.java
    ├── UserController.java
    └── admin/AdminDeviceController.java
```

**MyBatis XML mappers**: `src/main/resources/mapper/{MapperName}.xml`

## Key Patterns

### Response Format
- Success: `R.ok(data)` or `R.ok(data, "message")`
- Failure: `R.fail(message)` or `R.fail(message, data)`
- Pagination: Return `R<PageResult<T>>` using `PageResult.build(page, clazz)`

### Exception Handling
- Use `ServiceException.of(format, args)` for business exceptions
- `BaseException` for base exception type

### Complex SQL
- **Required**: Use XML mappers for complex queries (not `LambdaUpdateWrapper.setSql()`)
- Location: `src/main/resources/mapper/{MapperName}.xml`
- Steps: Define method in Mapper interface with `@Param`, write SQL in XML

### Authentication
- Sa-Token framework for token-based auth
- `SecurityUtils.getLoginUser()` to get current user in Service layer

## API Structure

| Module | Prefix | Description |
|--------|--------|-------------|
| Auth | `/api/v1/auth` | Login (wx/phone), logout, token refresh |
| User | `/api/v1/user` | User profile query/update |
| Devices | `/api/v1/devices` | Device bind/unbind/query |
| Records | `/api/v1/records` | Detection record upload/query |
| Feedbacks | `/api/v1/feedbacks` | User feedback |
| Admin | `/api/v1/admin/devices` | Admin device management |

## Database Tables

- `sys_user` - User information
- `t_device` - Device information
- `t_user_device` - User-device binding relation
- `t_record` - Detection records
- `t_feedback` - User feedback
- `sys_config` - System configuration

## Configuration Files

- `application.yaml` - Main config
- `config/application-dev.yaml` - Development environment
- `config/application-mock.yaml` - Mock data config
- `config/application-knife4j.yaml` - API documentation
- `logback-spring.xml` - Logging configuration

## Development Workflow

1. Create SQL script for new tables (`misc/sql/`)
2. Create Entity class in `domain/model/`
3. Create Mapper interface in `domain/mapper/`
4. Create XML mapper if complex SQL needed
5. Implement Service in `domain/service/impl/`
6. Add Controller endpoint in `web/`
7. Compile to verify syntax

## References

- API documentation: http://localhost:9093/api/doc.html (when running)
- Project docs: `misc/docs/` (产品需求文档, 接口开发进度, etc.)
- SQL scripts: `misc/sql/`
