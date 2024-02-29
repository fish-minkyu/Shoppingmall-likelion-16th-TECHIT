package com.example.shoppingmall.used.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(nullable = false)
  private String title;
  @Setter
  @Column(nullable = false)
  private String description;
  @Setter
  private String postImage;
  @Setter
  @Column(nullable = false)
  private Integer price;
  @Setter
  private ItemStatus itemStatus;

  @ManyToOne
  private UserEntity user;
}