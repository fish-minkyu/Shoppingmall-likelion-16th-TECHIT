# Trouble Shooting

## Global


## Auth

<details>
<summary><strong>Bean 간의 순환 의존성 발생</strong></summary>

### `문제`

회원가입 & 로그인 로직들을 구성하는 Bean들끼리 순환 의존성을 가져  
Bean이 제대로 생성이 되지 않아 문제가 발생하였다.

```java
APPLICATION FAILED TO START

Description:

The dependencies of some of the beans in the application context form a cycle:

authController defined in file [/Users/eominkyu/Desktop/shoppingmall/build/classes/java/main/com/example/shoppingmall/auth/AuthController.class]
┌─────┐
|  jpaUserDetailsManager defined in file [/Users/eominkyu/Desktop/shoppingmall/build/classes/java/main/com/example/shoppingmall/auth/JpaUserDetailsManager.class]
↑     ↓
|  webSecurityConfig defined in file [/Users/eominkyu/Desktop/shoppingmall/build/classes/java/main/com/example/shoppingmall/auth/config/WebSecurityConfig.class]
└─────┘

Action:

Relying upon circular references is discouraged and they are prohibited by default. Update your application to remove the dependency cycle between beans. As a last resort, it may be possible to break the cycle automatically by setting spring.main.allow-circular-references to true.
```

authController 빈이 jpaUserDetailsManager 빈에 의존하고  
jpaUserDetailsManager 빈이 webSecurityConfig 빈에 의존하고  
다시 webSecurityConfig 빈이 authController 빈에 의존하는 순환 참조가 발생한 것이다.

### `해결`

의존성 주입을 재구성하여 순환 의존성을 제거해주었다.  
JpaUserDetailsManager Bean을 만들려면,   
WebSecurityConfig에 있는 PasswordEncoder Bean이 필요하였다.

따라서, PasswordEncoder를 따로 빼주어 Bean으로 등록 후,  
JpaUserDeatilsManager가 의존성 주입이 되게 하여  
모든 Bean들이 정상적으로 등록이 되게 순환 의존성을 풀었다.

```java
- 의존성 주입 순서
PasswordEncoder -> JpaUserDetailsManager -> AuthController -> WebSecurityConfig
```

</details>

<details>
<summary><strong>관리자 계정으로 로그인 시, 403 Forbidden 에러</strong></summary>  



```markdown

```
⚠️ `Caution`  
해결했던 코드들을 지우고 다시 해보니 정상적으로 작동이 되었다.  
다른 작업과 동시에 Spring Security 작업을 하면서 알 수 없는 에러가 발생하였고  
해당 에러로 이동을 해서 출력을 해야하지만 WebSecurityConfig에서 에러로 가는 경로를 따로 설정해주지 않아   
권한이 없어 403 Forbidden이 발생한 것 같다.


### `문제`

일반 계정으로 로그인 할 시, 이상이 없다. 하지만, 관리자 계정으로 로그인할 시 되지 않았다.  
에러 코드는 403 Forbidden이 나오고 에러 로그는 확인이 되지 않았다.
```java
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

        .requestMatchers(
          "/user/profile",
          "/user/business"
        )
        .authenticated()

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
```


`💡 403 Forbidden`  
클라이언트가 요청한 리소드에 대한 필요한 권한이 없을 때 반환  
이 오류는 일반적으로 서버가 요청을 이해했지만 실행을 거부하는 경우 발생한다.


### `시도`

#### 전체 url 허용

```java
  .authorizeRequests()
                
                
                
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
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
       .anyRequest().authenticated()
      )
	   // ... 
    return http.build();
  }
}
```

### `해결`

CustomAuthenticationEntryPoint을 설정하고 WebSecurityConfig에 주입하니 바로 해결되었다.   
정확히 왜 해결이 되었는지 그 이유는 아직 모른다.

#### CustomAuthenticationEntryPoint

```java
// CustomAuthenticationEntryPoint
@Configuration
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }
}
```

#### WebSecurityConfig

```java
// WebSecurityConfig
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
	    // CustomAuthenticationEntryPoint 주입
      .exceptionHandling(e -> e
        .authenticationEntryPoint(customAuthenticationEntryPoint)
      )
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
      // ...
      )
	   // ... 
    return http.build();
  }
}
```

</details>


## Proposal