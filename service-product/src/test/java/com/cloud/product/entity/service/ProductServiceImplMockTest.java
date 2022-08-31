package com.cloud.product.entity.service;

import com.cloud.product.entity.Category;
import com.cloud.product.entity.Product;
import com.cloud.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplMockTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    Product computer;

    @BeforeEach
    void setUp() {

        computer = Product.builder()
                .id(1L)
                .name("computer")
                .category(Category.builder().id(1L).build())
                .price(12.5)
                .stock(5D)
                .build();
    }

    @Test
    void whenValidGetId_thenReturnProduct() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(computer));

        Product product = productService.getProduct(1L);

        assertEquals("computer", product.getName());
    }

    @Test
    void whenValidUpdateStock_thenReturnNewStock() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(computer));

        when(productRepository.save(computer))
                .thenReturn(computer);
        Product product = productService.updateStock(1L, 8D);

        assertEquals(13, product.getStock());
    }

}