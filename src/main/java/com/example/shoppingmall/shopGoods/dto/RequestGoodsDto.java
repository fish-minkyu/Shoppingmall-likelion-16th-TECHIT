package com.example.shoppingmall.shopGoods.dto;

import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import lombok.Getter;

@Getter
public class RequestGoodsDto {
  private Long id;
  private String goodsName;
  private String goodsDescription;
  private Integer goodsPrice;
  private Integer goodsStock;

  private ShopEntity shoppingMall;
}
