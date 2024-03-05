package com.example.shoppingmall.shopGoods.repo;

import com.example.shoppingmall.shopGoods.entity.GoodsEntity;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository
  extends JpaRepository<GoodsEntity, Long> {
  List<GoodsEntity> findAllByShoppingMall_IdAndGoodsNameContaining(Long shopId, String goodsName);

  List<GoodsEntity> findAllByShoppingMall_IdAndGoodsPriceBetween(Long shopId, Integer priceFloor, Integer priceCeil);

  GoodsEntity findByShoppingMall_IdAndId(Long shopId, Long goodsId);
}
