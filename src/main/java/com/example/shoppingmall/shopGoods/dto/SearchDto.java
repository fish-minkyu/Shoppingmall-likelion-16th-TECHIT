package com.example.shoppingmall.shopGoods.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class SearchDto {
  private String keyword;
  private Integer priceFloor;
  private Integer priceCeil;
}
