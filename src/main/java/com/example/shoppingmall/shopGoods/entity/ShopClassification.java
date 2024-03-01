package com.example.shoppingmall.shopGoods.entity;

import lombok.Getter;

@Getter
public enum ShopClassification {
  FASHION("의류"),
  BEAUTY("뷰티"),
  ELECTRONICS("전자제품"),
  FOOD("음식"),
  JEWELRY("쥬얼리");

  private String shopClasstification;

  ShopClassification(String shopClasstification) {
    this.shopClasstification = shopClasstification;
  }
}
