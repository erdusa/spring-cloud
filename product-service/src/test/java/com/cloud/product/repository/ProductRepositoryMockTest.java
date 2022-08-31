package com.cloud.product.repository;

import com.cloud.product.entity.Category;
import com.cloud.product.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

@DataJpaTest
class ProductRepositoryMockTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenFindByCategory_thenReturnListProduct() {
        Product product01 = Product.builder()
                .name("computer")
                .category(Category.builder().id(1L).build())
                .description("")
                .stock(10d)
                .price(1240.99)
                .status("Created")
                .createAt(new Date())
                .build();

        productRepository.save(product01);

        List<Product> founds = productRepository.findByCategory(product01.getCategory());

        Assertions.assertEquals(3, founds.size());
    }

}