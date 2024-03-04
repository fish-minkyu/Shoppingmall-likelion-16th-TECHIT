package com.example.shoppingmall.shopGoods.service;

import com.example.shoppingmall.ImageSort;
import com.example.shoppingmall.MultipartFileFacade;
import com.example.shoppingmall.shopGoods.ShopAuthenticationFacade;
import com.example.shoppingmall.shopGoods.dto.RequestGoodsDto;
import com.example.shoppingmall.shopGoods.dto.ResponseGoodsDto;
import com.example.shoppingmall.shopGoods.entity.GoodsEntity;
import com.example.shoppingmall.shopGoods.repo.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsService {
  private final GoodsRepository goodsRepository;
  private final ShopAuthenticationFacade shopAuth;
  private final MultipartFileFacade multipartFileFacade;

  // Create - (owner) 쇼핑몰 상품 등록
  public ResponseGoodsDto createGoods(RequestGoodsDto dto, MultipartFile goodsImage) {
  try {
    // 권한 확인
    shopAuth.checkShopAuthentication();

    // Goods 프로필 이미지 생성
    String requestPath
      = (String) multipartFileFacade.insertImage(ImageSort.GOODS, goodsImage);

    GoodsEntity newGoods = GoodsEntity.builder()
      .goodsName(dto.getGoodsName())
      .goodsDescription(dto.getGoodsDescription())
      .goodsPrice(dto.getGoodsPrice())
      .goodsStock(dto.getGoodsStock())
      .goodsImage(requestPath)
      .build();

    // 저장 및 반환
    return ResponseGoodsDto.fromEntity(goodsRepository.save(newGoods));
  } catch (Exception e) {
    log.error("err: {}", e.getMessage());
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
  }
  }

  // Update - (owner) 상품 내용 수정
  public ResponseGoodsDto updateGoods(Long goodsId, RequestGoodsDto dto) {
    // 권한 확인
    shopAuth.checkShopAuthentication();

    // targetGoods 찾기
    GoodsEntity targetGoods = goodsRepository.findById(goodsId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 상품 내용 수정
    targetGoods.setGoodsName(dto.getGoodsName());
    targetGoods.setGoodsDescription(dto.getGoodsDescription());
    targetGoods.setGoodsPrice(dto.getGoodsPrice());
    targetGoods.setGoodsStock(dto.getGoodsStock());

    // 반환
    return ResponseGoodsDto.fromEntity(
      goodsRepository.save(targetGoods)
    );
  }

  // Update - (owner) 상품 이미지 수정
  public ResponseGoodsDto updateGoodsImage(Long goodsId, MultipartFile goodsImage) {
    // 권한 확인
    shopAuth.checkShopAuthentication();

    // Goods 프로필 이미지 생성
    String requestPath
      = (String) multipartFileFacade.insertImage(ImageSort.GOODS, goodsImage);

    // targetGoods 찾기
    GoodsEntity targetGoods = goodsRepository.findById(goodsId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    targetGoods.setGoodsImage(requestPath);

    // 반환
    return ResponseGoodsDto.fromEntity(
      goodsRepository.save(targetGoods)
    );
  }

  // Delete - (owner) 상품 삭제
  public String deleteGoods(Long goodsId) {
    // 권한 확인
    shopAuth.checkShopAuthentication();

    // 삭제
    goodsRepository.deleteById(goodsId);

    return "done";
  }

  // Search - 쇼핑몰 상품 검색, 이름 & 가격 범위를 기준으로 상품 검색
}