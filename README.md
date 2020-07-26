# books
SpringBoot Rest Api tutorial with OpenApi 3.0 and Mysql.
Yes, yet another tutorial with "book, author and publisher" as entity.

# Topics covered
- Spring Boot Rest Api
- Swagger UI for visualizing APIs
- Error Handling
- Basic Authentication
- Mapper for POJO<->DTO 
- Logging
- Testing
    - Repositories using DataJpaTest
    - Services using Mockito
    - EndPoints using WebMvcTest
- Eclipse support

# Getting Started
## compile
./gradlew clean build

## quality code with checkstyle
./gradlew check

# How to execute
Different way to run this app
- execute using gradle: ./gradlew bootRun
- execute using fat jar: java -jar ./build/libs/books-1.0-SNAPSHOT.jar
- from Eclipse: import "Existing Gradle project" and "Run Application"

# API documentation
http://localhost:8081/books/swagger-ui.html

# Security
Basic Authentication with (user/password)

# Technologies used
- [Gradle 6.3](https://gradle.org/)
- [Java 8](http://www.oracle.com/technetwork/java/javaee/overview/index.html)
- [Spring Boot 2.3](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger OpeApi 3.0](https://swagger.io/specification/)
- [Mapstruct 1.3] (https://mapstruct.org/)
- [Spring Data JPA](https://projects.spring.io/spring-data-jpa)
- [Mysql connector 8.0](https://www.mysql.com/products/connector/)
- [HsqlDb](http://hsqldb.org/)
- [LogBack 1.2](https://logback.qos.ch/)
- [Mockito](https://site.mockito.org/)
