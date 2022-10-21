# Create this project (Gateway Server)
* Give one  gateway to all microservices in our system
* Dynamic routing, monitoring and security
* Load filters in hot

These are the steps to create a simple project for a Gateway Server

>IMPORTANT: First you have to have created the Config Server and Eureka Server

## Gradle configuration

Add these libraries in **build.gradle**

```gradle
dependencies {
	implementation 'org.springframework.cloud:spring-cloud-starter-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-gateway'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
	implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
}
```

## Annotations

Annotating the main class with **@EnableEurekaClient** for looking for Eureka

```java
@SpringBootApplication
@EnableEurekaClient
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

}
```
## Properties configuration

Create the file **gateway-service.yml** in folder that contains all files configuration where **Config Server** can read it. 

And put the follow configuration
```yml
server:
  port: 8080

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8099/eureka/

spring:
  cloud:
    gateway:
      discovery:
        locator:
          enable: true
      routes:
        - id: customer-service
          uri: lb://customer-service
          predicates:
            - Path=/customers/**
        - id: product-service
          uri: lb://product-service
          predicates:
            - Path=/products/**
        - id: shopping-service
          uri: lb://shopping-service
          predicates:
            - Path=/invoices/**
  ```
In resources, we change **application.properties** to **bootstrap.yml**

```yml
spring:
  application:
    name: gateway-service
  cloud:
    config:
      uri: http://localhost:8081
      username: root
      password: s3cr3t
```

# Enable Sleuth

We must enable Sleuth in all microservice and in gateway service in order to get a identifier for tracing request made 
to each microservice.

```gradle
dependencies {
	implementation 'org.springframework.cloud:spring-cloud-starter-sleuth'
}
```