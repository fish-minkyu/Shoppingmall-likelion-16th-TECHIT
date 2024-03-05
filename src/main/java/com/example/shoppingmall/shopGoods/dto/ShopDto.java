package com.example.shoppingmall.shopGoods.dto;

import com.example.shoppingmall.shopGoods.entity.ShopClassification;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
// (authenticated) 고객용 ShopDto
// "개설 신청"한 쇼핑몰 반환용 Dto - reason이 필요없어 빼줌
public class ShopDto {
  private String shopName;
  private String introduction;

  private ShopClassification shopClassification;

  // todo 상품 추가 시, private List<GoodsEntity> goods;도 추가

  public static ShopDto fromEntity(ShopEntity entity) {
    return new ShopDto(
      entity.getShopName(),
      entity.getIntroduction(),
      entity.getShopClassification()
    );
  }
}
