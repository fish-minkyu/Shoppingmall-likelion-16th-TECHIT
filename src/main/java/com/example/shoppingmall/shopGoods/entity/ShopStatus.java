package com.example.shoppingmall.shopGoods.entity;

import lombok.Getter;

@Getter
public enum ShopStatus {
  PREPAIRING("준비 중"),
  APPLICATION("개설 신청"),
  PERMISSION("허가"),
  REFUSE("거절");

  private String shopStatus;

  ShopStatus(String shopStatus) {
    this.shopStatus = shopStatus;
  }
}
