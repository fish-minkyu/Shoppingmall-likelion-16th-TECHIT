package com.example.shoppingmall.shopGoods.dto;

import com.example.shoppingmall.shopGoods.entity.ShopClassification;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import com.example.shoppingmall.shopGoods.entity.ShopStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
// (owner & Admin) 관리용 ShopDto
public class ManagementShopDto {
  private String shopName;
  private String introduction;

  private ShopClassification shopClassification;
  private ShopStatus shopStatus;

  private String reason;

  public static ManagementShopDto fromEntity(ShopEntity entity) {
    return new ManagementShopDto(
      entity.getShopName(),
      entity.getIntroduction(),
      entity.getShopClassification(),
      entity.getShopStatus(),
      entity.getReason()
    );
  }
}
