package com.example.shoppingmall.product.repo;

import com.example.shoppingmall.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
