package com.example.shoppingmall.auth.entity;

import lombok.Getter;


@Getter
public enum UserAuthority {
  HUMAN("비활성 사용자"),
  USER("일반 사용자"),
  BUSINESS("사업자 사용자"),
  ADMIN("관리자");

  private String authority;

  UserAuthority(String authority) {
    this.authority = authority;
  }
}
