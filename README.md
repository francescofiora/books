# books
SpringBoot Rest Api tutorial with OpenApi 3.0 and Mysql.
Yes, yet another tutorial with "book, author and publisher" as entities.

# Topics covered
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
    - Pojos and Dtos using OpenPojo
    - End to End Test with TestRestTemplate
- JMX
- Eclipse support

# Getting Started
## Using Docker to simplify development
The purpose of this tutorial is a Spring Boot tutorial, however I have inserted and a Dockerfile for MySql and phpMyAdmin. So you not need to install and configure them, with Docker that Dockerfile is sufficient.

    cd docker
    ./create_certificate.sh
    docker-compose up
 - you can use phpMyAdmin (http://localhost:8080/)

## Useful Docker command

 - docker exec -it docker_dbbooks.francescofiora.it_1 bash
 - docker exec -it docker_myadmin.francescofiora.it_1 bash

## check mysql SSL connection
    openssl s_client -connect localhost:3306 -tls1_2

## compile
    ./gradlew clean build

## Pitest
    ./gradlew pitest

## reports
    build/reports/checkstyle/main.html
    build/reports/checkstyle/test.html
    build/reports/tests/test/index.html
    build/reports/jacoco/test/html/index.html
    build/reports/pitest/index.html


# How to execute
- gradle: ./gradlew bootRun
- fat jar: java -jar ./build/libs/books-1.0-SNAPSHOT.jar
- Eclipse: import "Existing Gradle project" and "Run Application"

# How to execute with JMX support
- fat jar: java -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false  -jar ./build/libs/books-1.0-SNAPSHOT.jar
- Eclipse: import "Existing Gradle project", "Run Configuration ..." add java JMX configuration on "VM arguments" then "Run Application"

# url to connect with jconsole
service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi


# API documentation
https://localhost:8081/books/swagger-ui.html

# Security
 - HTTPS connection and Basic Authentication with (user/password)
 - JDBC with SSL connection.

# Technologies used
- [Gradle 7.0](https://gradle.org/)
- [Java 8](http://www.oracle.com/technetwork/java/javaee/overview/index.html)
- [Spring Boot 2.5](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger OpeApi 3.0](https://swagger.io/specification/)
- [Mapstruct 1.3](https://mapstruct.org/)
- [Lombok 1.18](https://projectlombok.org/)
- [Spring Data JPA](https://projects.spring.io/spring-data-jpa)
- [Mysql connector 8.0](https://www.mysql.com/products/connector/)
- [HsqlDb](http://hsqldb.org/)
- [LogBack 1.2](https://logback.qos.ch/)
- [Mockito](https://site.mockito.org/)
- [JUnit 5](https://junit.org/junit5/)
- [OpenPojo 0.8](https://github.com/OpenPojo)
- [Jacoco 0.8](https://www.jacoco.org/)
- [Pitest 1.5](https://pitest.org/)
