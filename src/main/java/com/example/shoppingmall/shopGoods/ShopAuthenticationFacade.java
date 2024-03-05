package com.example.shoppingmall.shopGoods;

import com.example.shoppingmall.auth.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import com.example.shoppingmall.shopGoods.entity.ShopStatus;
import com.example.shoppingmall.shopGoods.repo.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShopAuthenticationFacade {
  private final AuthenticationFacade auth;
  private final ShopRepository shopRepository;

  public ShopEntity checkShopAuthentication() {
    // 쇼핑몰 정보 가져오기
    UserEntity owner = auth.getAuth();
    ShopEntity targetShop = shopRepository.findByOwner_Id(owner.getId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    log.info("targetShop: {}", targetShop);

    // 쇼핑몰 상품 등록 가능한지 확인 - "허가" 상태일 때만 상품 생성
    if (!targetShop.getShopStatus().equals(ShopStatus.PERMISSION))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    return targetShop;
  }
}
