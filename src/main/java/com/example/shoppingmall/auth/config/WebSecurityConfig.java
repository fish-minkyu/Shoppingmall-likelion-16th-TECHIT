package com.example.shoppingmall.auth.config;

import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.service.JpaUserDetailsManager;
import com.example.shoppingmall.auth.jwt.JwtToeknFilter;
import com.example.shoppingmall.auth.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.ErrorResponse;

import java.io.PrintWriter;


@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final JwtTokenUtils jwtTokenUtils;
  private final JpaUserDetailsManager manager;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http
  ) throws Exception {
    http
      .exceptionHandling(e -> e
        .authenticationEntryPoint(customAuthenticationEntryPoint)
      )
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
        // 전체 권한
        .requestMatchers(
          "/user/register",
          "/auth/login"
        )
        .permitAll()

        // 인증된 사용자 권한(비활성화 계정 포함)
        .requestMatchers(
          "/user/profile",
          "/user/business"
        )
        .authenticated()

        // 일반 사용자 권한 이상
        .requestMatchers(
          "/item/enroll",
          "/item/{id}",
          "/{id}/modifying",
          "/item/{id}/removing",
          "/item/{id}/proposal/suggestion",
          "/item/{id}/proposal/list",
          "/item/{id}/proposal/paper",
          "/item/{id}/proposal/{proposalId}/accepted",
          "/item/{id}/proposal/{proposalId}/confirmation"
          
        )
        .hasAnyAuthority(
          UserAuthority.COMMON.getAuthority(),
          UserAuthority.BUSINESS.getAuthority(),
          UserAuthority.ADMIN.getAuthority()
        )

        // 관리자 권한
        .requestMatchers(
          "/admin/businessPending",
          "/admin/judgement/{id}",
          "/admin/test"
        )
        .hasAuthority(UserAuthority.ADMIN.getAuthority())
      )
      .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .addFilterBefore(
        new JwtToeknFilter(
          jwtTokenUtils,
          manager
        ),
        AuthorizationFilter.class
      )
      ;

    return http.build();
  }
}