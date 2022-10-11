# Configure Feing Client

These are the steps to configure Feign Client

>IMPORTANT: First you have to have created the Eureka Server

## Gradle configuration

Add feign library in **build.gradle**

```gradle
dependencies {
	implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
}
```

## Create model classes (dtos)

We create a package models and create classes like projects we are going to consume, for this case 
>model
> * Category
> * Customer
> * Product
> * Region

In Product model add property Customer, annotated with @Transient because it won't be saved in the database
```java
@Transient
private Customer customer;
```
Same thing in InvoiceItem model with Product
```java
@Transient
private Product product;
```

## Create Client Feigns

We create a package client when we're going put our feign clients

CustomerClient
```java
@FeignClient(value = "CUSTOMER-SERVICE", path = "/customers")
public interface CustomerClient {
    @GetMapping(value = "/{id}")
    ResponseEntity<Customer> getCustomer(@PathVariable("id") long id);
}
```

ProductClient
```java
@FeignClient(value = "PRODUCT-SERVICE", path = "/products")
public interface ProductClient {
@GetMapping("/{id}")
ResponseEntity<Product> getProduct(@PathVariable("id") Long id);

    @GetMapping("/{id}/stock")
    ResponseEntity<Product> updateStockProduct(@PathVariable("id") Long id,
                                               @RequestParam(value = "quantity") Double quantity);
}
```

We must annotate methods in each feign client like methods in endpoints 

## Annotations

Annotating the main class with **@EnableFeignClients**

```java
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class ShoppingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingServiceApplication.class, args);
    }

}
```
## Using Feign Clients

In each service we can use these clients for update stock, or read product information where return the item list from a Invoice

For instance:

```java
@Override
public Invoice getInvoice(Long id) {
    Invoice invoice = invoiceRepository.findById(id).orElse(null);
    if (invoice != null) {
        Customer customer = customerClient.getCustomer(invoice.getCustomerId()).getBody();
        invoice.setCustomer(customer);

        invoice.getItems().forEach(invoiceItem -> {
            Product product = productClient.getProduct(invoiceItem.getProductId()).getBody();
            invoiceItem.setProduct(product);
        });

    }
    return invoice;
}
```

# Configure Hystrix

## Gradle configuration

Add feign library in **build.gradle**

```gradle
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-hystrix'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-hystrix-dashboard'
}
```

## Annotations

We annotate the main class with @EnableHystrix and @EnableHystrixDashboard
```java
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@EnableHystrix
@EnableHystrixDashboard
public class ShoppingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingServiceApplication.class, args);
    }
}
```

## Configuration Properties

In our configuration property file (file in config server), we add this:

```yml
feign:
  hystrix:
    enabled: true
  circuitbreaker:
    enabled: true
management:
  endpoints:
    web:
      exposure:
        include: "*"
```
The management configuration is for "actuator"

## Create class fallback

```java
@Component
public class CustomerHystrixCallbackFactory implements CustomerClient{

    @Override
    public ResponseEntity<Customer> getCustomer(long id) {
        Customer customer = Customer.builder()
                .firstName("none")
                .lastName("none")
                .email("none")
                .photoUrl("none").build();
        return ResponseEntity.ok(customer);
    }
}
```

And set up the fallback value for @FeignClient
```java
@FeignClient(value = "CUSTOMER-SERVICE", path = "/customers", fallback = CustomerHystrixCallbackFactory.class)
public interface CustomerClient {
    @GetMapping(value = "/{id}")
    ResponseEntity<Customer> getCustomer(@PathVariable("id") long id);
}
```

We go to the browser and open the next url http://localhost:8093/hystrix for enter to hystrix dashboard

In the URL request in this page, we put http://localhost:8093/actuator/hystrix.stream and click on Monitor Stream