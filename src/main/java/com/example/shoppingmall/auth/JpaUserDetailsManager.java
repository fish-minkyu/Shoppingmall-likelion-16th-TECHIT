package com.example.shoppingmall.auth;

import com.example.shoppingmall.auth.entity.CustomUserDetails;
import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public JpaUserDetailsManager(
    UserRepository userRepository,
    PasswordEncoder passwordEncoder
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;

    // 관리자 계정 생성
    createUser(CustomUserDetails.builder()
      .userId("admin")
      .password(passwordEncoder.encode("admin"))
      .authority(UserAuthority.ADMIN)
      .build()
    );

    log.info("user authority: {}", UserAuthority.ADMIN);
  }

  // create
  public void createUser(CustomUserDetails user) {
    // 이미 존재하는 userId일 때, 에러 반환
    if (this.userExists(user.getUserId()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    try {
      UserEntity newUser = UserEntity.builder()
        .userId(user.getUserId())
        .password(passwordEncoder.encode(user.getPassword()))
        .username(user.getUsername())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .ageRange(user.getAgeRange())
        .phone(user.getPhone())
        .profile(user.getProfile())
        .build();

      if (user.isvalid()) {
        newUser.setAuthority(UserAuthority.HUMAN);
      } else {
        newUser.setAuthority(UserAuthority.USER);
      }

      userRepository.save(newUser);
    } catch (Exception e) {
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Read
  public CustomUserDetails loadUserByUserId(
    String userId
  ) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUserId(userId);

    return CustomUserDetails.builder()
      .id(user.getId())
      .userId(user.getUserId())
      .password(user.getPassword())
      .username(user.getUsername())
      .nickname(user.getNickname())
      .email(user.getEmail())
      .ageRange(user.getAgeRange())
      .phone(user.getPhone())
      .profile(user.getProfile())
      .build();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  public boolean userExists(String userId) {
    return userRepository.existsByUserId(userId);
  }
}
