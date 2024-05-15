package com.example.shoppingmall.product.dto;

import com.example.shoppingmall.product.entity.ProductEntity;
import com.example.shoppingmall.product.entity.ItemStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ResponseItemDto {
  private Long id;
  @Setter
  @Column(nullable = false)
  private String title;
  @Setter
  @Column(nullable = false)
  private String description;
  @Setter
  @Column(nullable = false)
  private String usedImage;
  @Setter
  @Column(nullable = false)
  private Integer price;
  @Setter
  private ItemStatus itemStatus;


  public static ResponseItemDto fromEntity(ProductEntity entity) {
    return new ResponseItemDto(
      entity.getId(),
      entity.getTitle(),
      entity.getDescription(),
      entity.getUsedImage(),
      entity.getPrice(),
      entity.getItemStatus()
    );
  }
}