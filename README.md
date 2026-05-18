# Student Management System - Backend API

Hệ thống backend quản lý sinh viên được xây dựng trên nền tảng **Quarkus** và **Java 17**, áp dụng kiến trúc **Monolithic RESTful API**. Hệ thống hỗ trợ đầy đủ các tính năng quản lý thực tế, phân quyền bảo mật bằng JWT, phân trang, tìm kiếm nâng cao và tích hợp tài liệu API tự động.

---

## 🛠 Công Nghệ Sử Dụng

* **Language:** Java 17
* **Framework:** Quarkus (Đảm bảo hiệu năng cao, khởi động nhanh)
* **Database:** MySQL
* **ORM:** Hibernate ORM với Panache (Active Record / Repository Pattern)
* **Build Tool:** Maven
* **Security:** Quarkus SmallRye JWT (Authentication & Authorization)
* **Validation:** Jakarta Validation (Kiểm tra dữ liệu đầu vào)
* **Documentation:** Swagger UI / OpenAPI 3
* **Testing:** Postman & Swagger UI

---

## 📂 Cấu Trúc Thư Mục Dự Án (Project Structure)

Dự án áp dụng kiến trúc phân lớp (**Layered Architecture**) chuẩn, tách biệt rõ ràng giữa các phân hệ quản lý:

```text
student-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── management/
│   │   │           ├── common/                  # Cấu hình chung, Exception toàn cục, Response chuẩn
│   │   │           │   ├── exception/
│   │   │           │   └── payload/
│   │   │           ├── config/                  # Cấu hình JWT, Security, OpenAPI
│   │   │           └── modules/                 # Các module nghiệp vụ của hệ thống
│   │   │               ├── auth/                # Module Authentication & Authorization
│   │   │               ├── student/             # Module Quản lý Sinh viên
│   │   │               ├── classroom/           # Module Quản lý Lớp học
│   │   │               ├── subject/             # Module Quản lý Môn học
│   │   │               └── score/               # Module Quản lý Điểm số
│   │   │                   ├── entity/          # Các thực thể Database (Entities)
│   │   │                   ├── dto/             # Data Transfer Objects (Request/Response)
│   │   │                   ├── repository/      # Tầng giao tiếp DB (PanacheRepository)
│   │   │                   ├── service/         # Tầng xử lý logic nghiệp vụ (Service Layer)
│   │   │                   └── resource/        # Tầng API endpoints (Controllers)
│   │   └── resources/
│   │       ├── application.properties           # File cấu hình chính của Quarkus
│   │       └── import.sql                       # Script seed dữ liệu mẫu tự động khi khởi chạy
├── pom.xml                                      # Quản lý dependency Maven
└── README.md

# student-management-system

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/student-management-system-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): Build RESTful web services and APIs using Jakarta REST (formerly JAX-RS)
- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Object-relational mapping with JPA/Hibernate for relational database access
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Bean validation using Hibernate Validator and Jakarta Validation annotations
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Generate OpenAPI schemas and serve Swagger UI for REST API documentation
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplified JPA/Hibernate data access layer with active record and repository patterns
- SmallRye JWT ([guide](https://quarkus.io/guides/security-jwt)): Secure your applications with JSON Web Token
- JDBC Driver - MySQL ([guide](https://quarkus.io/guides/datasource)): Connect to the MySQL database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)


[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)


### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
