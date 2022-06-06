# books
SpringBoot Rest Api tutorial with OpenApi 3.0 and Mysql.
Yes, yet another tutorial with "book, author and publisher" as entities.

### Topics covered
- Spring Boot Rest Api
- SSL connections
- Swagger UI for visualizing APIs
- Error Handling
- Basic Authentication
- Mapper for POJO<->DTO 
- Logging
- Testing
    - Repositories using DataJpaTest
    - Services using Mockito
    - EndPoints using WebMvcTest
    - POJOs and DTOs using OpenPojo
    - End to End Test with TestRestTemplate
- JMX
- Eclipse support

# Getting Started
### Using Docker for tests
There is a docker compose file to run MySql and phpMyAdmin.

    cd docker
    ./create_certificate.sh
    docker-compose up
 - phpMyAdmin (http://localhost:8080/)

### Useful Docker command

 - docker exec -it docker_dbbooks.francescofiora.it_1 bash

### Check mysql SSL connection
    openssl s_client -connect localhost:3306 -tls1_2

### Compile
    ./gradlew clean build

### Dependency-Check
    ./gradlew dependencyCheckAnalyze --info

### Pitest
    ./gradlew pitest

### SonarQube
Run SonarQube

    docker-compose -f docker/docker-compose-sonar.yml up

 - Sonarqube (http://localhost:9000/)

For SonarQube configuration follow this link: [Try Out SonarQube](https://docs.sonarqube.org/latest/setup/get-started-2-minutes/)

Scan project

    ./gradlew sonarqube \
    -Dsonar.projectKey=yourProjectKey \
    -Dsonar.login=yourAuthenticationToken

### Reports
    build/reports/checkstyle/main.html
    build/reports/checkstyle/test.html
    build/reports/tests/test/index.html
    build/reports/jacoco/test/html/index.html
    build/reports/dependency-check-report.html
    build/reports/pitest/index.html


# How to execute
- gradle: ./gradlew bootRun
- fat jar: java -jar ./build/libs/books-1.0-SNAPSHOT.jar
- Eclipse: import "Existing Gradle project" and "Run Application"

### Run the application with JMX support (Insecure connection)
    java -Dcom.sun.management.jmxremote.port=9999 \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=false \
    -jar ./build/libs/books-1.0-SNAPSHOT.jar

    jconsole service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi

### Run the application with JMX and SSL support
    java -Dcom.sun.management.jmxremote.port=9999 \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=true \
    -Dcom.sun.management.jmxremote.ssl.need.client.auth=false \
    -Djavax.net.ssl.keyStorePassword=mypass \
    -Djavax.net.ssl.keyStore=./docker/certs/localhost-keystore.jks \
    -jar ./build/libs/books-1.0-SNAPSHOT.jar

    jconsole -J-Djavax.net.ssl.trustStore=./docker/certs/truststore.ts \
    -J-Djavax.net.ssl.trustStorePassword=mypass \
    service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi

# API documentation
https://localhost:8081/swagger-ui.html

# Security
 - HTTPS connection and Basic Authentication with (user/password)
 - JDBC with SSL connection.

# Technologies used
- [Gradle 7.0](https://gradle.org/)
- [Java 11](https://openjdk.java.net/projects/jdk/11/)
- [Spring Boot 2.6](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger OpeApi 3.0](https://swagger.io/specification/)
- [Mapstruct 1.4](https://mapstruct.org/)
- [Lombok 1.18](https://projectlombok.org/)
- [Spring Data JPA](https://projects.spring.io/spring-data-jpa)
- [Mysql connector 8.0](https://www.mysql.com/products/connector/)
- [HsqlDb](http://hsqldb.org/)
- [Liquibase](https://www.liquibase.com/)
- [LogBack 1.2](https://logback.qos.ch/)
- [Mockito](https://site.mockito.org/)
- [JUnit 5](https://junit.org/junit5/)
- [OpenPojo 0.9](https://github.com/OpenPojo)
- [CheckStyle 8.44](https://checkstyle.sourceforge.io/)
- [Owasp Dependency Check 6.2](https://owasp.org/www-project-dependency-check/)
- [Jacoco 0.8](https://www.jacoco.org/)
- [Pitest 1.7](https://pitest.org/)
