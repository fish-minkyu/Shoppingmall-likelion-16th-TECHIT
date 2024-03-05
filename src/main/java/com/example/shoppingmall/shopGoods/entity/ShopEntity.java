package com.example.shoppingmall.shopGoods.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.purchase.entity.ItemOrderEntity;
import jakarta.persistence.*;
import lombok.*;

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

  @Setter
  private String shopName;
  @Setter
  private String introduction;

  @Setter
  @Enumerated(EnumType.STRING)
  private ShopClassification shopClassification;
  @Setter
  @Enumerated(EnumType.STRING)
  private ShopStatus shopStatus;

  @Setter
  private String reason;

  @Setter
  @OneToOne
  private UserEntity owner;

  @OneToMany(mappedBy = "shoppingMall")
  private List<GoodsEntity> goods = new ArrayList<>();

  @OneToMany
  private List<ItemOrderEntity> orders = new ArrayList<>();

  public boolean isNoNull() {
    return
      shopName != null &&
      introduction != null &&
      shopClassification != null;
  }
}
