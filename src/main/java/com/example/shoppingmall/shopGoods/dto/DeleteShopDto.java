package com.example.shoppingmall.shopGoods.dto;

import lombok.Getter;

@Getter
// (owner) 사업자 -> 관리자 에게 쇼핑몰 폐쇄 요청할 때 쓰는 Dto
public class DeleteShopDto {
  private String reason;
}
