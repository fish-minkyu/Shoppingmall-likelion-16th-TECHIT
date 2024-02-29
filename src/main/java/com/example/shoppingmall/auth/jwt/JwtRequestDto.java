package com.example.shoppingmall.auth.jwt;

import lombok.Getter;

@Getter
public class JwtRequestDto {
  private String loginId;
  private String password;
}