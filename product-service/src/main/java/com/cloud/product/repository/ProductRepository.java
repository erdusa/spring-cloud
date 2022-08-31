package com.cloud.product.repository;

import com.cloud.product.entity.Category;
import com.cloud.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);
}