package com.example.shoppingmall.shopGoods.controller;

import com.example.shoppingmall.shopGoods.service.ShopService;
import com.example.shoppingmall.shopGoods.dto.DeleteShopDto;
import com.example.shoppingmall.shopGoods.dto.ShopDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
  private final ShopService shopService;

  // Read - 쇼핑몰 조회 + 검색
  @PutMapping("/{shopId}/modifying")
  public ShopDto updateShop(
    @PathVariable("shopId") Long shopId,
    @RequestBody ShopDto dto
  ) {
    return shopService.updateShop(shopId, dto);
  }

  // Delete - 쇼핑몰 폐쇄 요청
  @DeleteMapping("/{shopId}/shutdown")
  public String deleteShop(
    @PathVariable("shopId") Long shopId,
    @RequestBody DeleteShopDto dto
    ) {
    return shopService.deleteShop(shopId, dto);
  }
}
