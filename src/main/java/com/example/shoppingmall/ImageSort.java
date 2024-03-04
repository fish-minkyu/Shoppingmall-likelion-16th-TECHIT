package com.example.shoppingmall;

import lombok.Getter;

@Getter
public enum ImageSort {
  USER("user"),
  USED("used"),
  GOODS("goods");

  private String imageSort;

  ImageSort(String imageSort) {
    this.imageSort = imageSort;
  }
}
