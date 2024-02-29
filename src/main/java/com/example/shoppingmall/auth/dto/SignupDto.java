package com.example.shoppingmall.auth.dto;

import com.example.shoppingmall.auth.entity.UserAuthority;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
public class SignupDto {
  private Long id;

  @Column(nullable = false)
  private String loginId;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String checkPassword;

  private String username;
  private String nickname;
  private String email;
  private String ageRange;
  private String phone;
  private String profile;

  private UserAuthority authority;
}