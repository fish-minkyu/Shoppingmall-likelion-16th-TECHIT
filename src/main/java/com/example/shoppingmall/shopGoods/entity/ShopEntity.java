package com.example.shoppingmall.shopGoods.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShopEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String shopName;
  private String introduction;

  @Enumerated(EnumType.STRING)
  private ShopClassification shopClassification;
  @Enumerated(EnumType.STRING)
  private ShopStatus shopStatus;

  private String reason;

  @OneToOne
  private UserEntity owner;

  @OneToMany(mappedBy = "shoppingMall")
  private List<GoodsEntity> goods = new ArrayList<>();
}
