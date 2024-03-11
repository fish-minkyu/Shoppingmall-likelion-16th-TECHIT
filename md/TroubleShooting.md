# Trouble Shooting

## Global


## Auth

<details>
<summary><strong>Bean 간의 순환 의존성 발생</strong></summary>

### 문제

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

authController 빈이

jpaUserDetailsManager 빈에 의존하고

jpaUserDetailsManager 빈이 webSecurityConfig 빈에 의존하고

다시 webSecurityConfig 빈이 authController 빈에 의존하는 순환 참조가 발생한 것이다.

## 해결

의존성 주입을 재구성하여 순환 의존성을 제거해주었다.

JpaUserDetailsManager Bean을 만들려면

WebSecurityConfig에 있는 PasswordEncoder Bean이 필요하였다.

따라서, PasswordEncoder를 따로 빼주어 Bean으로 등록 후,

JpaUserDeatilsManager가 의존성 주입이 되게 하여 모든 Bean들이 정상적으로 등록이 되게

순환 의존성을 풀었다.

```java
- 의존성 주입 순서
PasswordEncoder -> JpaUserDetailsManager -> AuthController -> WebSecurityConfig
```


</details>

## Proposal