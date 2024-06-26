package com.example.shoppingmall.config;

import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.service.JpaUserDetailsManager;
import com.example.shoppingmall.auth.jwt.JwtToeknFilter;
import com.example.shoppingmall.auth.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


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
            HttpMethod.POST,
            "/users/**"
        )
        .permitAll()

        // 인증된 사용자 권한(비활성화 계정 포함)
        .requestMatchers(
            HttpMethod.PUT,
            "/users/**"
        )
        .authenticated()

        // 일반 사용자 권한 이상
        .requestMatchers(
          "/products/**",
          "/purchases/**",
          "/shops/**"
        )
        .hasAnyAuthority(
          UserAuthority.COMMON.getAuthority(),
          UserAuthority.BUSINESS.getAuthority(),
          UserAuthority.ADMIN.getAuthority()
        )

        // 사업자(owner) 권한
        .requestMatchers(
          "/goods/**"
        )
        .hasAuthority(UserAuthority.BUSINESS.getAuthority())

        // 관리자 권한
        .requestMatchers(
          "/admins/**"
        )
        .hasAuthority(UserAuthority.ADMIN.getAuthority())

        // 그 외 모든 경로 권한 허용
        .anyRequest().permitAll()
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