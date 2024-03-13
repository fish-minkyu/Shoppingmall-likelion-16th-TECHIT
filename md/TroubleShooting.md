# Trouble Shooting

## Global

<details>
<summary><strong>에러 시, Unauthorized만 나오고 그 외 오류는 안나오는 이유</strong></summary>

아직 작성 중...

</details>

---

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

---

## Used

<details>
<summary><strong>createItem, 클래스 캐스팅 문제</strong></summary>

### `문제`

Item 로직에서 Create를 할 때, 문제가 발생했다.

```java
  public void createItem(ItemDto dto) {
    try {
      UserEntity user 
				= (UserEntity) auth.getAuth().getPrincipal();

      log.info("UserEntity: {}", user);

      ItemEntity newItem = ItemEntity.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .postImage(dto.getPostImage())
        .price(dto.getPrice())
        .itemStatus(ItemStatus.SELLING)
        .user(user)
        .build();

      itemRepository.save(newItem);
    } catch (Exception e) {
			log.error("error", e);
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

UserEntity로 클래스 캐스팅 중 에러가 발생하여 `java.lang.ClassCastException`이 출력되고 있었다.  
getAuth()로 Authentication을 얻어와서 getPrincipal()을 하면 Object를 반환하므로  
UserEntity로 바로 캐스팅을 해줘도 문제가 없을 것이라 생각했다.

### `해결`

Facade Pattern을 이용한  
getAuth() 메서드의 반환타입이 바로 UserEntity가 나올 수 있게 바꿔주었다.

```java
@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
  private final UserRepository userRepository;
  public UserEntity getAuth() {
    CustomUserDetails customUserDetails =
      (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return userRepository.findByLoginId(customUserDetails.getLoginId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
```

### `알게된 점`

1. 바로 UserEntity로 캐스팅을 할 수 없고, UserDetails를 구현 한 CustomUserDetails로  
   먼저 클래스 캐스팅을 해주고 그 다음 단계로 거쳐가야 한다는 것을 알게 되었다.
2. 위 과정은 SpringSecurity의 설정을 어떻게 주느냐에 따라 달라지는 것임도 알게 되었다.

</details>

---

## Proposal

<details>
<summary><strong>enum 타입 ProposalStatus의 최신 변경사항이 적용이 되지 않음</strong></summary>

### `문제`

```java
import lombok.Getter;

@Getter
public enum ProposalStatus {
  WAITING("대기"),
  ACCEPTED("수락"),
  DENIED("거절"),
  CONFIRMATION("구매확정");

  private String proposalStatus;

  ProposalStatus(String proposalStatus) {
    this.proposalStatus = proposalStatus;
  }
}
```

위 코드에서 CONFIRMATION("구매확정") 코드를 추가 후 해당 상태로 변경을 할려고 하면  
아래 에러가 발생했다.

```java
2024-02-29T15:25:02.819+09:00 ERROR 4197 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : [SQLITE_CONSTRAINT_CHECK] A CHECK constraint failed (CHECK constraint failed: proposal_status between 0 and 2)
2024-02-29T15:25:02.826+09:00 ERROR 4197 --- [nio-8080-exec-1] c.e.s.proposal.ProposalService           : errpr {}:

org.springframework.orm.jpa.JpaSystemException: could not execute statement [[SQLITE_CONSTRAINT_CHECK] A CHECK constraint failed (CHECK constraint failed: proposal_status between 0 and 2)] [insert into proposal_entity (buyer_id,item_id,proposal_status,seller_id) values (?,?,?,?)]
```

해당 에러는 proposal_status 필드의 값이 “WAITING”, “ACCEPTED”, “DENIED” 중 하나가  
아니라는 것을 의미하는데 DB로 사용하고 있는 SQLite가 ProposalStatus의 최신 변경사항을  
받아들이지 못하는 것이란 판단을 했다.

### `해결`

```java
  jpa:
    hibernate:
      ddl-auto: create
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    show-sql: true
```

jpa.hibernate.ddl-auto의 속성을 `update`에서 `create`로 변경하였다.

### `알게된 점`

1. SQLite는 `ENUM`타입을 직접 지원하지 않기 때문에 CHECK 제약 조건을 사용하여 특정 문자열 값들만  
   허용하는 방식으로 `ENUM`과 유사한 기능을 구현한다.
2. SQLite에서는 테이블에 이미 존재하는 CHECK 제약 조건을 직접 수정할 수 없다.
   그래서 수정사항이 생긴다면 기존 테이블을 삭제 후 새로운 테이블을 생성해줘야 한다.

</details>

---

## ShopGoods

<details>
<summary><strong>@ModelAttribute가 제대로 작동하기 위한 방법</strong></summary>

### `문제`

이미지를 받기 위해 사용한 @ModelAttribute가 예상대로 작동해주지 않아 문제가 발생했다.

```java
  // Create - (owner) 쇼핑몰 상품 등록
  @PostMapping("/enroll")
  public ResponseGoodsDto createGoods(
    @ModelAttribute RequestGoodsDto dto,
    @RequestParam("goodsImage") MultipartFile goodsImage
    ) {
    return goodsService.createGoods(dto, goodsImage);
  }
```

```java
@Getter
public class RequestGoodsDto {
  private String goodsName;
  private String goodsDescription;
  private Integer goodsPrice;
  private Integer goodsStock;
}
```

```java
2024-03-05T13:58:22.938+09:00 ERROR 2683 --- [nio-8080-exec-1] c.e.s.shopGoods.service.GoodsService     : err: not-null property references a null or transient value : com.example.shoppingmall.shopGoods.entity.GoodsEntity.goodsDescription
```

### `해결`

@ModelAttribute가 제대로 작동하려면, RequestGoodsDto 클래스에 기본 생성자와 Setter 메소드가 필요해  
@Setter와 @NoArgsConstructor를 붙였다.

```java
@Getter
@Setter
@NoArgsConstructor
public class RequestGoodsDto {
  private String goodsName;
  private String goodsDescription;
  private Integer goodsPrice;
  private Integer goodsStock;
}
```

</details>