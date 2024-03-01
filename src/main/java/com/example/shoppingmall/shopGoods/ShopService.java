package com.example.shoppingmall.shopGoods;

import com.example.shoppingmall.auth.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.shopGoods.dto.ShopDto;
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
  private final AuthenticationFacade auth;

  //todo test 해봐야 함
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

  // Update - 쇼핑몰 주인이 쇼핑몰의 이름, 소개, 분류를 수정한다.
  public ShopDto updateShop(Long shopId, ShopDto dto) {
    try {
      // 해당 로그인한 유저의 정보를 가져온다.
      UserEntity shopOwner = auth.getAuth();

      // 해당 shop의 정보를 가져온다.
      ShopEntity targetShop = shopRepository.findById(shopId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // 로그인 유저와 shop의 주인이 같은지 확인한다.
      if (!shopOwner.getId().equals(targetShop.getOwner().getId()))
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

      // 같다면, shop의 정보를 수정한다.
      targetShop.setShopName(dto.getShopName());
      targetShop.setIntroduction(dto.getIntroduction());
      targetShop.setShopClassification(dto.getShopClassification());

      // 정보를 저장하고 ShopDto를 반환한다.
      return ShopDto.fromEntity(shopRepository.save(targetShop));

    } catch (Exception e) {
      log.error("err: {}", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
