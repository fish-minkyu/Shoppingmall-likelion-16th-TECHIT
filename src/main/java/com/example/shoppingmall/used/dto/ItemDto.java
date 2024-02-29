package com.example.shoppingmall.used.dto;

import com.example.shoppingmall.used.entity.ItemEntity;
import com.example.shoppingmall.used.entity.ItemStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ItemDto {
  private Long id;
  @Setter
  @Column(nullable = false)
  private String title;
  @Setter
  @Column(nullable = false)
  private String description;
  @Setter
  @Column(nullable = false)
  private String postImage;
  @Setter
  @Column(nullable = false)
  private Integer price;
  @Setter
  private ItemStatus itemStatus;

  private Long userUniqueId;

  public static ItemDto fromEntity(ItemEntity entity) {
    return new ItemDto(
      entity.getId(),
      entity.getTitle(),
      entity.getDescription(),
      entity.getPostImage(),
      entity.getPrice(),
      entity.getItemStatus(),
      entity.getId()
    );
  }
}