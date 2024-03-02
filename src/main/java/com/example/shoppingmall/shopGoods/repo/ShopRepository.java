package com.example.shoppingmall.shopGoods.repo;

import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
  @Query("SELECT s FROM ShopEntity s LEFT JOIN FETCH s.orders o ORDER BY o.createdAt DESC")
  List<ShopEntity> findAllWithRecentOrder();

  List<ShopEntity> findAllByShopName(String shopName);

  List<ShopEntity> findAllByShopClassification(String shopClassification);
}
