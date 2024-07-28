# books
SpringBoot Rest Api tutorial with OpenApi 3.0 and Mysql.
Yes, yet another tutorial with "book, author and publisher" as entities.

### Topics covered
- Spring Boot Rest Api
- Swagger UI for visualizing APIs
- Error Handling
- JSON Web Token Authentication
- Mapper for POJO<->DTO 
- Logging
- Testing
    - Repositories using DataJpaTest
    - Services using Mockito
    - EndPoints using WebMvcTest
    - POJOs and DTOs using OpenPojo
    - End to End Test with TestRestTemplate
    - Integration test using TestContainers and Cucumber

# Getting Started
### Compile
    ./gradlew clean build

### Dependency-Check
    ./gradlew dependencyCheckAnalyze --info

### Pitest
    ./gradlew pitest

### SonarQube
Run SonarQube

    docker compose -f docker/docker-compose-sonar.yml up

 - Sonarqube (http://localhost:9000/)

For SonarQube configuration follow this link: [Try Out SonarQube](https://docs.sonarqube.org/latest/setup/get-started-2-minutes/)

Scan project

    ./gradlew sonarqube \
    -Dsonar.projectKey=yourProjectKey \
    -Dsonar.token=yourAuthenticationToken

### Reports
    build/reports/checkstyle/main.html
    build/reports/checkstyle/test.html
    build/reports/tests/test/index.html
    build/reports/jacoco/test/html/index.html
    build/reports/dependency-check-report.html
    build/reports/pitest/index.html

### Using Docker for tests
There is a docker compose file to run MySql and phpMyAdmin.

    docker compose -f docker/docker-compose.yml up
 - phpMyAdmin (http://localhost:8080/)

# How to execute
 - gradle: ./gradlew bootRun
 - fat jar: java -jar ./build/libs/books-1.0-SNAPSHOT.jar

### Run the application with JMX support
    java -Dendpoints.jmx.enabled=true \
    -Dcom.sun.management.jmxremote.port=9999 \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=false \
    -jar ./build/libs/books-1.0-SNAPSHOT.jar

    jconsole service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi

# API documentation
http://localhost:8081/swagger-ui.html

## System Integration Test environment

### Create Docker images
    ./gradlew jibDockerBuild

### Manual tests - execute all applications with Docker

    docker compose -f docker/docker-compose.yml -f docker/docker-compose-book.yml up

### Integration Test

    cd books-integration-test/
    ./gradlew clean build

# Technologies used
- [Gradle 8.5](https://gradle.org)
- [Java 17](https://openjdk.java.net/projects/jdk/17)
- [Spring Boot 3.3](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger OpeApi 3.0](https://swagger.io/specification)
- [Mapstruct 1.5](https://mapstruct.org)
- [Lombok 1.18](https://projectlombok.org)
- [Spring Data JPA](https://projects.spring.io/spring-data-jpa)
- [Mysql connector 8.0](https://www.mysql.com/products/connector)
- [HsqlDb](http://hsqldb.org)
- [Liquibase 4.29](https://www.liquibase.com)
- [LogBack 1.5](https://logback.qos.ch)
- [Mockito](https://site.mockito.org)
- [JUnit 5](https://junit.org/junit5)
- [OpenPojo 0.9](https://github.com/OpenPojo)
- [CheckStyle 8.44](https://checkstyle.sourceforge.io)
- [Owasp Dependency Check 8.4](https://owasp.org/www-project-dependency-check)
- [Jacoco 0.8](https://www.jacoco.org)
- [Pitest 1.15](https://pitest.org)
- [TestContainers 1.20](https://www.testcontainers.org)
- [Cucumber 7.18](https://cucumber.io)
