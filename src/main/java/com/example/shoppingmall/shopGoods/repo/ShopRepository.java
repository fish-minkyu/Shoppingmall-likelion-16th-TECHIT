package com.example.shoppingmall.shopGoods.repo;

import com.example.shoppingmall.shopGoods.entity.ShopClassification;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import com.example.shoppingmall.shopGoods.entity.ShopStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
  // 쇼핑몰 중, 최신 주문 순으로 나열
  @Query("SELECT s FROM ShopEntity s LEFT JOIN FETCH s.orders o ORDER BY o.createdAt DESC")
  List<ShopEntity> findAllWithRecentOrder();

  // 쇼핑몰 이름에 따른 검색 결과
  List<ShopEntity> findAllByShopName(String shopName);

  // 쇼핑몰 분류에 따른 검색 결과
  List<ShopEntity> findAllByShopClassification(String shopClassification);

  // 개설 신청된 쇼핑몰 목록 보기 or 폐쇄 요청 쇼핑몰 목록 보기
  List<ShopEntity> findAllByShopStatus(ShopStatus shopStatus);

  // 개설 신청된 쇼핑몰 하나 보기 or 폐쇄 요청 쇼핑몰 하나 보기
  ShopEntity findByIdAndShopStatus(Long shopId, ShopStatus shopStatus);

  // (owner) owner.getId()로 해당 쇼핑몰 불러오기
  Optional<ShopEntity> ㅈ(Long ownerId);
}
