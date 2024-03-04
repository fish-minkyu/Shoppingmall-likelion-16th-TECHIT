package com.example.shoppingmall.auth.service;

import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.shopGoods.service.ShopService;
import com.example.shoppingmall.shopGoods.dto.DecesionDto;
import com.example.shoppingmall.shopGoods.dto.ManagementShopDto;
import com.example.shoppingmall.shopGoods.dto.ShopDto;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import com.example.shoppingmall.shopGoods.entity.ShopStatus;
import com.example.shoppingmall.shopGoods.repo.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
  private final UserRepository userRepository;
  private final ShopService shopService;
  private final ShopRepository shopRepository;

  // Read - readAll
  public List<UserEntity> pending() {
    return userRepository.findAllByAuthority(UserAuthority.PENDING);
  }

  // Update - 일반 사용자 -> 사업자 변경 수락 여부
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

  // Read - "개설 신청"한 쇼핑몰 목록 보기
  public List<ShopDto> readApplicationList() {
    List<ShopDto> dtoList = new ArrayList<>();

    List<ShopEntity> targetShop
      = shopRepository.findAllByShopStatus(ShopStatus.APPLICATION);

    for (ShopEntity entity : targetShop) {
      dtoList.add(ShopDto.fromEntity(entity));
    }

    return dtoList;
  }

  // Read - "개설 신청"한 쇼핑몰 하나 보기
  public ShopDto readApplicationOne(Long shopId) {
    ShopEntity targetShop
      = shopRepository.findByIdAndShopStatus(shopId, ShopStatus.APPLICATION);

    return ShopDto.fromEntity(targetShop);
  }

  // Update - "개설 신청" 허가 or 거절
  public ManagementShopDto approvalOrRefuse(Long shopId, DecesionDto dto) {
    // 해당 쇼핑몰을 찾는다.
    ShopEntity targetShop = shopRepository.findById(shopId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // approval에 따라 로직을 분기 처리한다.
    if (dto.getApproval()) {
      // true, 허가
      targetShop.setShopStatus(ShopStatus.PERMISSION);
    } else {
      // false, 불허 및 불가 사유 저장
      targetShop.setShopStatus(ShopStatus.REFUSE);
      targetShop.setReason(dto.getReason());
    }

    // 저장 및 반환
    return ManagementShopDto.fromEntity(shopRepository.save(targetShop));
  }

  // Read - 사업자가 신청한 폐쇄 요청 리스트 확인하기
  public List<ManagementShopDto> readShutdownList() {
    List<ManagementShopDto> managementShopDtoList
      = new ArrayList<>();

    List<ShopEntity> targetEntities = shopRepository.findAllByShopStatus(ShopStatus.SHUTDOWN);
    for (ShopEntity entity : targetEntities) {
      managementShopDtoList.add(ManagementShopDto.fromEntity(entity));
    }

    return managementShopDtoList;
  }

  // Read - 사업자가 신청한 폐쇄 요청 하나 확인하기
  public ManagementShopDto readShutdownOne(Long shopId) {
    ShopEntity targetEntity
      = shopRepository.findByIdAndShopStatus(shopId, ShopStatus.SHUTDOWN);

    return ManagementShopDto.fromEntity(targetEntity);
  }

  // Update - 폐쇄 요청 수락
  public ManagementShopDto acceptShutdown(Long shopId) {
    ShopEntity targetEntity
      = shopRepository.findByIdAndShopStatus(shopId, ShopStatus.SHUTDOWN);

    targetEntity.setShopStatus(ShopStatus.DEAD);

    return ManagementShopDto.fromEntity(targetEntity);
  }
}