package com.example.shoppingmall.shopGoods.dto;

import lombok.Getter;

@Getter
// (Admin) 관리자가 쇼핑몰을 허가 or 불허할 때 사용되는 Dto
public class DecesionDto {
  private Boolean approval;
  private String reason;
}
