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
  @Column(nullable = false)
  private String goodsName;
  @Setter
  @Column(nullable = false)
  private String goodsDescription;
  @Setter
  @Column(nullable = false)
  private String goodsImage;
  @Setter
  @Column(nullable = false)
  private Integer goodsPrice;
  @Setter
  @Column(nullable = false)
  private Integer goodsStock;

  @ManyToOne(fetch = FetchType.LAZY)
  private ShopEntity shoppingMall;
}
