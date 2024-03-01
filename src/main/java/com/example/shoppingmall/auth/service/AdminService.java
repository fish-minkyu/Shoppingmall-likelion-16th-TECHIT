package com.example.shoppingmall.auth.service;

import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.shopGoods.ShopService;
import com.example.shoppingmall.shopGoods.repo.ShopRepository;
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
  private final ShopService shopService;

  // Read - readAll
  public List<UserEntity> pending() {
    return userRepository.findAllByAuthority(UserAuthority.PENDING);
  }

  // Update
  public UserDto judge(Long userId, Boolean judgement) {
    try {
      // 해당 사업자 신청 user를 불러온다.
      UserEntity targetEntity = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // judgement에 따라 authority를 바꿔준다
      if (judgement) {
        // true, 대기 중 -> 사업자 사용자
        targetEntity.setAuthority(UserAuthority.BUSINESS);

        // 준비 중인 쇼핑몰 create
        shopService.createShop(userId);

        return UserDto.fromEntity(userRepository.save(targetEntity));
      }

      // false, 대기 중 -> 일반 사용자
      targetEntity.setAuthority(UserAuthority.COMMON);
      targetEntity.setBusinessNumber(null);
      return UserDto.fromEntity(targetEntity);
    } catch (Exception e) {
      log.error("err: {}", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}