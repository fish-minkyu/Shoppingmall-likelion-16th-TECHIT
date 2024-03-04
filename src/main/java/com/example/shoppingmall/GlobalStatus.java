package com.example.shoppingmall;

import lombok.Getter;

@Getter
public enum GlobalStatus {
  USER("user"),
  USED("used"),
  GOODS("goods"),
  SHOP("shop");

  private String globalStatus;

  GlobalStatus(String globalStatus) {
    this.globalStatus = globalStatus;
  }
}
