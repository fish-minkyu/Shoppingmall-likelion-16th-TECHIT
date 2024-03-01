package com.example.shoppingmall.shopGoods.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  private String goodsName;
  @Setter
  private String goodsDescription;
  @Setter
  private String goodsImage;
  @Setter
  private Integer goodsPrice;
  @Setter
  private Integer goodsStock;

  @ManyToOne
  private ShopEntity shoppingMall;
}
