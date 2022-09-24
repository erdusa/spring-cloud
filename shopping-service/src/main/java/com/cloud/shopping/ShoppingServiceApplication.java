package com.cloud.shopping;

import com.cloud.shopping.client.CustomerClient;
import com.cloud.shopping.client.ProductCliente;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = {CustomerClient.class, ProductCliente.class})
@EnableEurekaClient
@SpringBootApplication
public class ShoppingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingServiceApplication.class, args);
    }

}
