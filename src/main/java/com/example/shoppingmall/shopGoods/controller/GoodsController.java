package com.example.shoppingmall.shopGoods.controller;

import com.example.shoppingmall.shopGoods.dto.RequestGoodsDto;
import com.example.shoppingmall.shopGoods.dto.ResponseGoodsDto;
import com.example.shoppingmall.shopGoods.dto.SearchDto;
import com.example.shoppingmall.shopGoods.dto.SearchResponseGoodsDto;
import com.example.shoppingmall.shopGoods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
  private final GoodsService goodsService;

  // Create - (owner) 쇼핑몰 상품 등록
  @PostMapping("/enroll")
  public ResponseGoodsDto createGoods(
    @ModelAttribute RequestGoodsDto dto,
    @RequestParam("goodsImage") MultipartFile goodsImage
    ) {
    return goodsService.createGoods(dto, goodsImage);
  }

  // Update - (owner) 상품 내용 수정 및 상품 이미지 수정
  @PutMapping("/modifying/{goodsId}")
  public ResponseGoodsDto updateGoodsAndImage(
    @PathVariable("goodsId") Long goodsId,
    @ModelAttribute RequestGoodsDto dto,
    @RequestParam("goodsImage") MultipartFile goodsImage
  ) {
    return goodsService.updateGoodsAndImage(goodsId, dto, goodsImage);
  }

  // Delete - (owner) 상품 삭제
  @DeleteMapping("/removing/{goodsId}")
  public String deleteGoods(
    @PathVariable("goodsId") Long goodsId
  ) {
    return goodsService.deleteGoods(goodsId);
  }

  // todo 못하겠음
  // Search - 쇼핑몰 상품 검색: 이름 & 가격 범위를 기준으로 상품 검색
  @GetMapping("/search/{shopId}")
  public List<SearchResponseGoodsDto> searchGoodsOfShop(
    @PathVariable("shopId") Long shopId,
    @RequestParam("category") String category,
    @ModelAttribute SearchDto dto
    ) {
    return goodsService.searchGoodsOfShop(shopId, category, dto);
  }
}
