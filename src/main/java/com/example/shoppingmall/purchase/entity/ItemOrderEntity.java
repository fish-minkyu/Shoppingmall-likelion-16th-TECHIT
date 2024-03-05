package com.example.shoppingmall.purchase.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import com.example.shoppingmall.used.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrderEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private ItemEntity item;

  private Integer amount;
  private String status;
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private ShopEntity shop;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity customer;
}
