package com.example.shoppingmall.product.dto;

import com.example.shoppingmall.product.entity.ItemStatus;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RequestItemDto {
  private Long id;
  @Setter
  @Column(nullable = false)
  private String title;
  @Setter
  @Column(nullable = false)
  private String description;
  @Setter
  @Column(nullable = false)
  private Integer price;
  @Setter
  private ItemStatus itemStatus;
}
