# Create this project (Eureka Server)
* Server for register and localization microservice instances
* Each microservice instance (Eureka Client) notifies "heartbeats" every 30s
* Each microservice has a copy cached of Eureka registry
* Works in cluster mode
* Self-prevention mode

These are the steps to create a simple project for a Eureka Server

>IMPORTANT: First you have to have created the Config Server

## Gradle configuration

Add these libraries in **build.gradle**

```gradle
dependencies {
	implementation 'org.springframework.cloud:spring-cloud-starter-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
	implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
}
```

## Annotations

Annotating the main class with **@EnableEurekaServer**

```java
@EnableEurekaServer
@SpringBootApplication
public class RegistryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryServiceApplication.class, args);
    }

}
```
## Properties configuration

Create the file **registry-service.yml** in folder that contains all files configuration where **Config Server** can read it. 

And put the follow configuration
```yml
server:
  port: 8099

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  ```
In resources, we change **application.properties** to **bootstrap.yml**

```yml
spring:
  application:
    name: registry-service
  cloud:
    config:
      uri: http://localhost:8081
      username: root
      password: s3cr3t
```

For testing the Eureka Service, we must put this url in a browser

```link
http://localhost:8099
```
## Setting in each microservice

In build.gradle, add this dependency
```gradle
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
```

Now, annotate the main class with **@EnableEurekaClient**
```java
@EnableEurekaClient
@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

}
```

Finally, in *config file* (read it for Config Server) add the follow:

```yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8099/eureka
```

Now, when we run our microservice, it registers itself into Eureka Server