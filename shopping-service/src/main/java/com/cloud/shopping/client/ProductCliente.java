package com.cloud.shopping.client;

import com.cloud.shopping.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "PRODUCT-SERVICE")
public interface ProductCliente {
    @GetMapping("/{id}")
    ResponseEntity<Product> getProduct(@PathVariable("id") Long id);

    @GetMapping("/{id}/stock")
    ResponseEntity<Product> updateStockProduct(@PathVariable("id") Long id,
                                               @RequestParam(value = "quantity") Double quantity);
}
