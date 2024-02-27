package com.example.shoppingmall.auth.service;

import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
  private final UserRepository userRepository;

  // Read - readAll
  public List<UserEntity> pending() {
    return userRepository.findAllByAuthority(UserAuthority.PENDING);
  }

  // Update
  public UserDto judge(Long id, Boolean judgement) {
    // 해당 user를 불러온다.
    Optional<UserEntity> optionalUser = userRepository.findById(id);

    if (optionalUser.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    UserEntity targetEntity = optionalUser.get();

    // judgement에 따라 authority를 바꿔준다,
    if (judgement) {
      targetEntity.setAuthority(UserAuthority.BUSINESS);
      return UserDto.fromEntity(userRepository.save(targetEntity));
    }

    return UserDto.fromEntity(targetEntity);
  }
}
