package com.example.shoppingmall.auth.controller;

import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.service.AdminService;
import com.example.shoppingmall.shopGoods.dto.DecesionDto;
import com.example.shoppingmall.shopGoods.dto.ManagementShopDto;
import com.example.shoppingmall.shopGoods.dto.ShopDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
  private final AdminService adminService;

  // Read - SELECT * FROM userEntity WHERE authority = UserAuthority.PENDING
  @GetMapping("/businessPending")
  public List<UserEntity> pending() { //todo 반환을 UserEntity가 아닌 다른 걸로 바꿔줘야 함, password가 보이면 안됨
    return adminService.pending();
  }

  // Update - 일반 사용자 -> 사업자 변경 수락 여부
  @PutMapping("/judgement/{userId}")
  public UserDto judgement(
    @PathVariable("userId") Long userId,
    @RequestParam("judgement") Boolean judgement
  ) {
    return adminService.judge(userId, judgement);
  }

  // Read - "개설 신청"한 쇼핑몰 목록 보기
  @GetMapping("/list/application")
  public List<ShopDto> readApplicationList() {
    return adminService.readApplicationList();
  }

  // Read - "개설 신청"한 쇼핑몰 하나 보기
  @GetMapping("/view/application/{shopId}")
  public ShopDto readApplicationOne(
    @PathVariable("shopId") Long shopId
  ) {
    return adminService.readApplicationOne(shopId);
  }

  // todo test
  // Update - "개설 신청" 허가 or 거절
  @PutMapping("/approval/{shopId}")
  public ManagementShopDto approval(
    @PathVariable("shopId") Long shopId,
    @RequestBody DecesionDto dto
    ) {
    return adminService.approvalOrRefuse(shopId, dto);
  }

  // Read - 사업자가 신청한 폐쇄 요청 리스트 확인하기
  @GetMapping("/list/shutdown")
  public List<ManagementShopDto> readShutdownList() {
    return adminService.readShutdownList();
  }

  // Update - 폐쇄 요청 수락
  @PutMapping("/accept/shutdown/{shopId}")
  public ManagementShopDto acceptShutdown(
    @PathVariable("shopId") Long shopId
  ) {
    return adminService.acceptShutdown(shopId);
  }
}
