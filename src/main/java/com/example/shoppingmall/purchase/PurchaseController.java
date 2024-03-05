package com.example.shoppingmall.purchase;

import com.example.shoppingmall.purchase.dto.RequestBuyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/purchase/{shopId}")
@RequiredArgsConstructor
public class PurchaseController {
  private final PurchaseService purchaseService;

  // Create - 상품과 구매 수량을 기준으로 구매 요청
  @PostMapping("/application/{goodsId}")
  public void createPurchase(
    @PathVariable("shopId") Long shopId,
    @PathVariable("goodsId") Long goodsId,
    @RequestBody RequestBuyDto dto
    ) {

  }
}
