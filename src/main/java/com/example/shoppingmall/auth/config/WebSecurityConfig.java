package com.example.shoppingmall.auth.config;

import com.example.shoppingmall.auth.JpaUserDetailsManager;
import com.example.shoppingmall.auth.jwt.JwtToeknFilter;
import com.example.shoppingmall.auth.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http
  ) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(
          "/user/register",
          "/auth/login"
        )
        .permitAll()
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
