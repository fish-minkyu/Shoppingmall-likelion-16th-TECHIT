package com.example.shoppingmall.shopGoods.controller;

import com.example.shoppingmall.shopGoods.ShopService;
import com.example.shoppingmall.shopGoods.dto.ShopDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
  private final ShopService shopService;
  // Read

  // todo test 필요
  // Update
  @PutMapping("/{shopId}/modifying")
  public ShopDto updateShop(
    @PathVariable("shopId") Long shopId,
    @RequestBody ShopDto dto
  ) {
    return shopService.updateShop(shopId, dto);
  }

  // Delete

}
