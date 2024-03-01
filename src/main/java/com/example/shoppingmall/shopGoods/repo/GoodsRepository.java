package com.example.shoppingmall.shopGoods.repo;

import com.example.shoppingmall.shopGoods.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {
}
