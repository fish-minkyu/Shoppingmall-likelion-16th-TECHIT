package com.example.shoppingmall.shopGoods.dto;

import com.example.shoppingmall.shopGoods.entity.ShopClassification;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ShopDto {
  private String shopName;
  private String introduction;

  private ShopClassification shopClassification;

  public static ShopDto fromEntity(ShopEntity entity) {
    return new ShopDto(
      entity.getShopName(),
      entity.getIntroduction(),
      entity.getShopClassification()
    );
  }
}
