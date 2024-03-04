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

  // Read - 쇼핑몰 조회
  // todo 향 후, Pagenation으로 리팩토링
  // todo, test - 이름과 분류로 검색해보기
  @GetMapping("/list")
  public List<ShopDto> readShop(
    @RequestParam(value = "category", defaultValue = "empty") String category,
    @RequestParam(name = "keyword", required = false) String keyword
  ) {
    return shopService.readShop(category, keyword);
  }

  // Update - owner: 쇼핑몰 수정
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
