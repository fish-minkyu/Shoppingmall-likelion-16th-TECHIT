package com.example.shoppingmall.auth.dto;

import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.entity.UserEntity;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private Long id;
  private String loginId;
  private String username;
  private String nickname;
  private String email;
  private String ageRange;
  private String phone;
  private String profile;
  private UserAuthority authority;

  public static UserDto fromEntity(UserEntity entity) {
    return new UserDto(
      entity.getId(),
      entity.getLoginId(),
      entity.getUsername(),
      entity.getNickname(),
      entity.getEmail(),
      entity.getAgeRange(),
      entity.getPhone(),
      entity.getProfile(),
      entity.getAuthority()
    );
  }
}