package com.example.shoppingmall.shopGoods.repo;

import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
}
