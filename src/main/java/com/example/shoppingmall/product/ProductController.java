package com.example.shoppingmall.product;

import com.example.shoppingmall.product.dto.RequestItemDto;
import com.example.shoppingmall.product.dto.ResponseItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/used")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;
  // Create - 제목, 설명, 최소가격 or 제목, 설명, 최소가격, 대표 이미지
  @PostMapping("/enroll")
  public ResponseItemDto createItem(
    @ModelAttribute RequestItemDto dto,
    @RequestParam("usedImage") MultipartFile usedImage
    ) {
    return productService.createItem(dto, usedImage);
  }

  // Read
  @GetMapping("/{id}")
  public ResponseItemDto readOne(
    @PathVariable("id") Long id
  ) {
    return productService.readOne(id);
  }

  // Update - 제목, 설명, 최소가격, 대표 이미지
  @PutMapping("/modifying/{usedId}")
  public ResponseItemDto updateItem(
    @PathVariable("usedId") Long usedId,
    @ModelAttribute RequestItemDto dto,
    @RequestParam("usedImage") MultipartFile usedImage
  ) {
    return productService.updateItem(usedId, dto, usedImage);
  }

  // Update - 이미지 추가하기
  @PutMapping("/image/{usedId}")
  public ResponseItemDto updateImage(
    @PathVariable("usedId") Long usedId,
    MultipartFile usedImage
  ) {
    return productService.updateImage(usedId, usedImage);
  }

  // Delete
  @DeleteMapping("/removing/{id}")
  public String deleteItem(
    @PathVariable("id") Long id
  ) {
    return productService.deleteItem(id);
  }

  @GetMapping("/test")
  public void test() {
    productService.test();
  }
}