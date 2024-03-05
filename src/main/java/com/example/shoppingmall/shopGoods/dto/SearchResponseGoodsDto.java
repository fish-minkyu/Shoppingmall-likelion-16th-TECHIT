package com.example.shoppingmall.shopGoods.dto;

import com.example.shoppingmall.shopGoods.entity.GoodsEntity;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResponseGoodsDto {
  private Long id;
  private String goodsName;
  private String goodsDescription;
  private String goodsImage;
  private Integer goodsPrice;
  private Integer goodsStock;

  private ShopEntity shoppingMall;

  public static SearchResponseGoodsDto fromEntity(GoodsEntity entity) {
    return new SearchResponseGoodsDto(
      entity.getId(),
      entity.getGoodsName(),
      entity.getGoodsDescription(),
      entity.getGoodsImage(),
      entity.getGoodsPrice(),
      entity.getGoodsStock(),
      entity.getShoppingMall()
    );
  }
}
