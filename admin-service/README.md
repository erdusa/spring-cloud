# Configure Admin Server and Actuator

With this configuration we can monitor our microservices instances 

## Gradle configuration
Add feign library in **build.gradle**

```gradle
dependencies {
	implementation 'de.codecentric:spring-boot-admin-starter-server'
}
```

## Annotations

Annotating the main class with **@EnableAdminServer**

```java
@SpringBootApplication
@EnableAdminServer
public class AdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }

}
```

## Config File

In application.properties set:
```properties
spring.application.name=admin-service
server.port=8086
```

## Configuration for each microservice

### Build.gradle

```gradle
dependencies {
    ...
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation group: 'de.codecentric', name: 'spring-boot-admin-starter-client', version: '2.7.5'
    ...
}
```

### bootstrap.yml

```yml
spring:
  
  boot:
    admin:
      client:
        url: http://localhost:8086
```

### NAME-service.yml

In configuration file that the Config-Server read, add configuration for enable actuator

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

After start this project and each microservice, we open http://localhost:8086