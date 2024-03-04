package com.example.shoppingmall.shopGoods.dto;

import com.example.shoppingmall.shopGoods.entity.GoodsEntity;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseGoodsDto {
  private Long id;
  private String goodsName;
  private String goodsDescription;
  private String goodsImage;
  private Integer goodsPrice;
  private Integer goodsStock;

  // 굳이 전체 쇼핑몰의 정보를 불러올 필요는 없으므로 Long으로 타입 선언
  private Long shoppingMallId;

  public static ResponseGoodsDto fromEntity(GoodsEntity entity) {
    return new ResponseGoodsDto(
      entity.getId(),
      entity.getGoodsName(),
      entity.getGoodsDescription(),
      entity.getGoodsImage(),
      entity.getGoodsPrice(),
      entity.getGoodsStock(),
      entity.getShoppingMall().getId()
    );
  }
}
