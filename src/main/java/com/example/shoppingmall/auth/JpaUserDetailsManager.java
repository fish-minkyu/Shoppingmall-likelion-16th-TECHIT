package com.example.shoppingmall.auth;

import com.example.shoppingmall.auth.entity.CustomUserDetails;
import com.example.shoppingmall.auth.entity.UserEntity;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JpaUserDetailsManager implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  // create
  public void createUser(CustomUserDetails user) {
    if (this.userExists(user.getUserId()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    try {

      UserEntity newUser = UserEntity.builder()
        .userId(user.getUserId())
        .password(passwordEncoder.encode(user.getPassword()))
        .build();

    } catch (Exception e) {
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // delete



  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  public boolean userExists(String userId) {
    return userRepository.existsByUserId(userId);
  }
}
