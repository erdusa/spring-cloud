# Create this project (Spring Config)

These are the steps to create a simple project for a Spring Cloud

## Gradle configuration

Add these libraries in **build.gradle**

```gradle
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.cloud:spring-cloud-config-server'
	implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.springframework.security:spring-security-test'
}
```
```gradle
dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}
```

## Annotations

Annotating the main class with **@EnableConfigServer**

```java
@EnableConfigServer
@SpringBootApplication
public class ConfigServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }
}
```
## Properties configuration

Now we put all config property files, for each microservice, in a folder in order to centralize the configurations, and create a repository in GitHub for this folder (in this example is: <https://github.com/digitallab-academy/ms-course-youtube.git>).

Each file must be renamed like the name we will put in the file configuration property for each microservice.

Example:
```
config-data
* customer-service.yml
* product-service.yml
* shopping-service.yml
```

```yml
spring:
  application:
    name: SAME_NAME_CONFIG_FILE
```

In resources, we change **application.properties** to **bootstrap.yml**

```yml
server:
  port: 8081

spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/digitallab-academy/ms-course-youtube.git # Repo with the application.yml
          searchPaths: config-data
          username: ${GIT_USER} # Configuring in environment variables for the project in intelliJ
          password: ${GIT_PASSWORD} # Configuring in environment variables for the project in intelliJ
          default-label: "master"
  security:
    user:
      name: root
      password: s3cr3t
```

For testing the Config Service, we put this url in postman

```link
http://root:s3cr3t@localhost:8081/product-service/default
```
And we get the configuration for the *microservice* **product-service** in the profile **default**

## Setting in each microservice

In build.gradle, add this dependency
```gradle
implementation 'org.springframework.cloud:spring-cloud-starter-config'
implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
```
In resources, we change **application.properties** to **bootstrap.yml** and delete all content and put this content

```yml
spring:
  application:
    name: customer-service #SAME_NAME_CONFIG_FILE
  cloud:
    config:
      uri: http://localhost:8081 #Config Server URL
      username: root
      password: s3cr3t
```

Now, when we run our microservice, it starts reading its configuration file from the Config Server (GitHub)