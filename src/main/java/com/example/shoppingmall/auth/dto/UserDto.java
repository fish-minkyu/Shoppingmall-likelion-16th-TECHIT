package com.example.shoppingmall.auth.dto;

import com.example.shoppingmall.auth.entity.UserAuthority;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private Long id;
  private String userId;
  private String password;
  private String checkPassword;
  private String username;
  private String nickname;
  private String email;
  private String ageRange;
  private String phone;
  private String profile;
  private UserAuthority authority;
}
