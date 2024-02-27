package com.example.shoppingmall.auth;

import com.example.shoppingmall.auth.entity.CustomUserDetails;
import com.example.shoppingmall.auth.jwt.JwtRequestDto;
import com.example.shoppingmall.auth.jwt.JwtResponseDto;
import com.example.shoppingmall.auth.jwt.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {
  private final JwtTokenUtils jwtTokenUtils;
  private final JpaUserDetailsManager manager;
  private final PasswordEncoder passwordEncoder;

  // JWT 토큰 발행
  @PostMapping("/issue")
  public JwtResponseDto issueJwt(
    @RequestBody JwtRequestDto dto
    ) {
    // 1. 사용자가 제공한 userId가 저장된 사용자인지 판단
    if (!manager.userExists(dto.getUserId()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    CustomUserDetails customUserDetails
      = manager.loadUserByUserId(dto.getUserId());

    // 2. 비밀번호 대조
    if (!passwordEncoder
      .matches(dto.getPassword(), customUserDetails.getPassword()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    // 3. JWT 발급
    String jwt = jwtTokenUtils.generateToken(customUserDetails);
    JwtResponseDto response = new JwtResponseDto();
    response.setToken(jwt);
    return response;
  }

  // JWT 토큰 유효성 검사
  @GetMapping("/token")
  public Claims validateToken(
    @RequestHeader("Authorizatoin") String token
  ) {
    if (!jwtTokenUtils.validate(token))
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);

    return jwtTokenUtils.parseClaims(token);
  }
}
