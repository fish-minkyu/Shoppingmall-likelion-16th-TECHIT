package com.example.shoppingmall.used.repo;

import com.example.shoppingmall.used.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}
