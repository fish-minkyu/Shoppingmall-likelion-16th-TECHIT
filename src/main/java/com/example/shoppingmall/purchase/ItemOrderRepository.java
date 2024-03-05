package com.example.shoppingmall.purchase;

import com.example.shoppingmall.purchase.entity.ItemOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOrderRepository extends JpaRepository<ItemOrderEntity, Long> {
}
