package com.example.shoppingmall.auth;

import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final JpaUserDetailsManager service;

  @PostMapping("/register")
  public String createUser(
   @RequestBody UserDto dto
  ) {
    //todo 수정 필요
    service.createUser(CustomUserDetails.builder()
      .userId(dto.getUserId())
      .password(dto.getPassword())
      .build()
    );
    return "done";
  }
}
