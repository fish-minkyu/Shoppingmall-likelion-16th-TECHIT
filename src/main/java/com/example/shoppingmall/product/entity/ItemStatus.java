package com.example.shoppingmall.product.entity;

import lombok.Getter;

@Getter
public enum ItemStatus {
  SELLING("판매중"),
  SOLD("판매완료");

  private String itemStatus;

  ItemStatus(String itemStatus) {
    this.itemStatus = itemStatus;
  }
}