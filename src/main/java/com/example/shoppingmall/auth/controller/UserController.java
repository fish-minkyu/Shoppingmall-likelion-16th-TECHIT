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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
      .build()
    );
  }

  // 유저 프로필 이미지 업로드
  @PutMapping("/profile/image")
  public UserDto updateProfileImage(
    @RequestParam("image") MultipartFile imageFile
  ) {
    return service.updateProfileImage(imageFile);
  }

  // 유저 사업자 계정 신청
  @PutMapping("/business")
  public String businessApplication(
    @RequestBody BusinessApplicationDto dto
    ) {
    return service.businessApply(dto);
  }
}