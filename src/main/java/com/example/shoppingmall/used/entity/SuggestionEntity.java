package com.example.shoppingmall.used.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String purchaser;
  private String buyer;
  private String suggestion;

  @ManyToOne
  private ItemEntity item;

  @ManyToOne
  private UserEntity user;
}
