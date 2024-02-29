package com.example.shoppingmall.auth.controller;

import com.example.shoppingmall.auth.jwt.JwtRequestDto;
import com.example.shoppingmall.auth.jwt.JwtResponseDto;
import com.example.shoppingmall.auth.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final JpaUserDetailsManager manager;

  // login
  @PostMapping("/login")
  public JwtResponseDto login(
    @RequestBody JwtRequestDto dto
    ) {
    return manager.issueJwt(dto);
  }
}