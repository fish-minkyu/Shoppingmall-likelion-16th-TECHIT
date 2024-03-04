package com.example.shoppingmall.shopGoods.controller;

import com.example.shoppingmall.shopGoods.dto.RequestGoodsDto;
import com.example.shoppingmall.shopGoods.dto.ResponseGoodsDto;
import com.example.shoppingmall.shopGoods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
  private final GoodsService goodsService;

  // todo test 필요
  // Create - (owner) 쇼핑몰 상품 등록
  @PostMapping("/enroll")
  public ResponseGoodsDto createGoods(
    @RequestBody RequestGoodsDto dto,
    @RequestParam("goodsImage") MultipartFile goodsImage
    ) {

    return goodsService.createGoods(dto, goodsImage);
  }

  // todo test 필요
  // Update - (owner) 상품 내용 수정
  @PutMapping("/modifying/{goodsId}")
  public ResponseGoodsDto updateGoods(
    @PathVariable("goodsId") Long goodsId,
    @RequestBody RequestGoodsDto dto
  ) {
    return goodsService.updateGoods(goodsId, dto);
  }

  // todo test 필요
  // Update - (owner) 상품 이미지 수정
  @PutMapping("/image/{goodsId}")
  public ResponseGoodsDto updateGoodsImage(
    @PathVariable("goodsId") Long goodsId,
    @RequestParam("goodsImage") MultipartFile goodsImage
  ) {
    return goodsService.updateGoodsImage(goodsId, goodsImage);
  }

  // todo test 필요
  // Delete - (owner) 상품 삭제
  @DeleteMapping("/removing/{goodsId}")
  public String deleteGoods(
    @PathVariable("goodsId") Long goodsId
  ) {
    return goodsService.deleteGoods(goodsId);
  }

  // todo test 필요
  // Search - 쇼핑몰 상품 검색, 이름 & 가격 범위를 기준으로 상품 검색
}
