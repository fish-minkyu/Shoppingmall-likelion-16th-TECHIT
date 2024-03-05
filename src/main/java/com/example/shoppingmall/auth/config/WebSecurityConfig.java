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
          "/user/login"
        )
        .permitAll()

        // 인증된 사용자 권한(비활성화 계정 포함)
        .requestMatchers(
          "/user/profile",
          "/user/business",
          "/user/profile/image"
        )
        .authenticated()

        // 일반 사용자 권한 이상
        .requestMatchers(
          "/used/enroll",
          "/used/{usedId}",
          "/used/modifying/{usedId}",
          "/used/removing/{id}",
          "/used/image/{usedId}",
          "/used/{id}/proposal/suggestion",
          "/used/{id}/proposal/list",
          "/used/{id}/proposal/paper",
          "/used/{id}/proposal/{proposalId}/accepted",
          "/used/{id}/proposal/{proposalId}/confirmation",
          "/used/{id}/proposal/{proposalId}/canceled",

          "/used/test/{id}",
          "/used/test",

          "/shop/{shopId}/modifying",
          "/shop/list",
          "/shop/{shopId}/shutdown"

        )
        .hasAnyAuthority(
          UserAuthority.COMMON.getAuthority(),
          UserAuthority.BUSINESS.getAuthority(),
          UserAuthority.ADMIN.getAuthority()
        )

        // 사업자(owner) 권한
        .requestMatchers(
          "/goods/enroll",
          "/goods/modifying/{goodsId}",
          "/goods/removing/{goodsId}",
          "/goods/search/{shopId}"
        )
        .hasAuthority(UserAuthority.BUSINESS.getAuthority())

        // 관리자 권한
        .requestMatchers(
          "/admin/businessPending",
          "/admin/judgement/{userId}",
          "/admin/list/application",
          "/admin/view/application/{shopId}",
          "/admin/list/shutdown",
          "/admin/view/shutdown/{shopId}",
          "/admin/approval/{shopId}",
          "/admin/list/shutdown",
          "/admin/accept/shutdown/{shopId}"
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