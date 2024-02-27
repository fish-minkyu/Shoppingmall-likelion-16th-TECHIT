package com.example.shoppingmall.auth;

import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final JpaUserDetailsManager service;

  // signup
  @PostMapping("/register")
  public String createUser(
   @RequestBody UserDto dto
  ) {
    // 비밀번호 & 비밀번호 체크 일치 확인
    // => createUser는 CustomUserDetails 타입이므로 UserDto를 쓰는 컨트롤러에서 해결
    if (!dto.getPassword().equals(dto.getCheckPassword()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    service.createUser(CustomUserDetails.builder()
        .userId(dto.getUserId())
        .password(dto.getPassword())
        .username(dto.getUsername())
        .nickname(dto.getNickname())
        .email(dto.getEmail())
        .ageRange(dto.getAgeRange())
        .phone(dto.getPhone())
        .profile(dto.getProfile())
        .build()
    );
    return "done";
  }
}
