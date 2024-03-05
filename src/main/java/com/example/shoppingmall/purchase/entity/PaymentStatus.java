package com.example.shoppingmall.purchase.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {
  OFFER("구매 요청"),
  TRANSFER("송금 완료"),
  CALL("구매 수락"),
  REFUND("구매 취소");

  private String paymentStatus;

  PaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }
}
