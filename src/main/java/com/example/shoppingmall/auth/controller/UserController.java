package com.example.shoppingmall.auth.controller;

import com.example.shoppingmall.auth.dto.BusinessApplicationDto;
import com.example.shoppingmall.auth.dto.SignupDto;
import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.CustomUserDetails;
import com.example.shoppingmall.auth.jwt.JwtRequestDto;
import com.example.shoppingmall.auth.jwt.JwtResponseDto;
import com.example.shoppingmall.auth.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final JpaUserDetailsManager service;

  // signup
  @PostMapping("/register")
  public String createUser(
   @RequestBody SignupDto dto
  ) {
    log.info("/user/register");
    log.info("signupDto {}", dto);
    service.createUser(dto);

    return "done";
  }

  // login
  @PostMapping("/login")
  public JwtResponseDto login(
    @RequestBody JwtRequestDto dto
  ) {
    return service.issueJwt(dto);
  }

  // userInfo update
  @PutMapping("/profile")
  public UserDto updateUser(
    @RequestBody UserDto dto
  ) {
    return service.updateUser(CustomUserDetails.builder()
      .username(dto.getUsername())
      .nickname(dto.getNickname())
      .email(dto.getEmail())
      .ageRange(dto.getAgeRange())
      .phone(dto.getPhone())
      .profile(dto.getProfile())
      .build()
    );
  }

  @PutMapping("/business")
  public String businessApplication(
    @RequestBody BusinessApplicationDto dto
    ) {
    return service.businessApply(dto);
  }
}