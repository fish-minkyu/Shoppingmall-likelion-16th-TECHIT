package com.example.shoppingmall.used.dto;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.used.entity.ItemStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class ItemDto {
  private Long id;
  @Setter
  private String title;
  @Setter
  private String description;
  @Setter
  private String postImage;
  @Setter
  private Integer price;
  @Setter
  private ItemStatus itemStatus;

  private UserEntity user;
}
