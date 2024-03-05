package com.example.shoppingmall.shopGoods.dto;

import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestGoodsDto {
  private String goodsName;
  private String goodsDescription;
  private Integer goodsPrice;
  private Integer goodsStock;
}
