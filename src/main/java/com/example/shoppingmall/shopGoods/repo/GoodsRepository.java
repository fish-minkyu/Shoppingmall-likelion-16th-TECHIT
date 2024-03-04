package com.example.shoppingmall.shopGoods.repo;

import com.example.shoppingmall.shopGoods.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {
}
