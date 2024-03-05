package com.example.shoppingmall.purchase;

import com.example.shoppingmall.auth.AuthenticationFacade;
import com.example.shoppingmall.purchase.dto.RequestBuyDto;
import com.example.shoppingmall.shopGoods.repo.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {
  private final GoodsRepository goodsRepository;
  private final ItemOrderRepository orderRepository;
  private final AuthenticationFacade auth;

  // Create - 상품과 구매 수량을 기준으로 구매 요청
  public void createPurchase(
    Long shopId,
    Long goodsId,
    RequestBuyDto dto
  ) {
    // 해당 쇼핑몰의 상품을 찾는다.
    goodsRepository.findByShoppingMall_IdAndId(shopId, goodsId);

    // 해당 상품의 재고가 dto.getCount()보다 많은지 확인한다.

    // 주문 생성

    // 저장 및 반환
  }
}
