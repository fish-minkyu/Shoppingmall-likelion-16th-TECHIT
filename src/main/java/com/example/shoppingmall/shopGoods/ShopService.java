package com.example.shoppingmall.shopGoods;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import com.example.shoppingmall.shopGoods.entity.ShopStatus;
import com.example.shoppingmall.shopGoods.repo.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {
  private final ShopRepository shopRepository;
  private final UserRepository userRepository;

  // Create - 일반 사용자가 사업자 사용자로 전환될 때 준비중 상태의 쇼핑몰이 추가된다.
  public void createShop(Long userId) {
    try {
      // userId로 해당 사업자 신청 유저를 불러온다.
      UserEntity businessUser = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // "준비 중" 쇼핑몰을 만든다.
      shopRepository.save(ShopEntity.builder()
        .shopName("준비 중")
        .shopStatus(ShopStatus.PREPAIRING)
        .owner(businessUser)
        .build()
      );
    } catch (Exception e) {
      log.error("err: {}", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Read

  // Update
  public void updateShop() {

  }
}
