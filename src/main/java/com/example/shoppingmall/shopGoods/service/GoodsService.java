package com.example.shoppingmall.shopGoods.service;

import com.example.shoppingmall.ImageSort;
import com.example.shoppingmall.MultipartFileFacade;
import com.example.shoppingmall.shopGoods.ShopAuthenticationFacade;
import com.example.shoppingmall.shopGoods.dto.RequestGoodsDto;
import com.example.shoppingmall.shopGoods.dto.ResponseGoodsDto;
import com.example.shoppingmall.shopGoods.dto.SearchDto;
import com.example.shoppingmall.shopGoods.dto.SearchResponseGoodsDto;
import com.example.shoppingmall.shopGoods.entity.GoodsEntity;
import com.example.shoppingmall.shopGoods.entity.ShopEntity;
import com.example.shoppingmall.shopGoods.repo.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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
    // 권한 확인 및 쇼핑몰 정보 가져오기
    ShopEntity targetShop = shopAuth.checkShopAuthentication();

    // Goods 프로필 이미지 생성
    String requestPath
      = (String) multipartFileFacade.insertImage(ImageSort.GOODS, goodsImage);

    GoodsEntity newGoods = GoodsEntity.builder()
      .goodsName(dto.getGoodsName())
      .goodsDescription(dto.getGoodsDescription())
      .goodsPrice(dto.getGoodsPrice())
      .goodsStock(dto.getGoodsStock())
      .goodsImage(requestPath)
      .shoppingMall(targetShop)
      .build();

    // 저장 및 반환
    return ResponseGoodsDto.fromEntity(goodsRepository.save(newGoods));
  } catch (Exception e) {
    log.error("err: {}", e.getMessage());
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
  }
  }

  // Update - (owner) 상품 내용 수정 및 상품 이미지 수정
  public ResponseGoodsDto updateGoodsAndImage(
    Long goodsId, RequestGoodsDto dto, MultipartFile goodsImage
  ) {
// 권한 확인 및 쇼핑몰 정보 가져오기
    ShopEntity targetShop = shopAuth.checkShopAuthentication();

    // targetGoods 찾기
    GoodsEntity targetGoods = goodsRepository.findByShoppingMall_IdAndId(targetShop.getId(), goodsId);

    if (goodsImage == null) {
      // 상품 내용 수정
      targetGoods.setGoodsName(dto.getGoodsName());
      targetGoods.setGoodsDescription(dto.getGoodsDescription());
      targetGoods.setGoodsPrice(dto.getGoodsPrice());
      targetGoods.setGoodsStock(dto.getGoodsStock());

      // 반환
      return ResponseGoodsDto.fromEntity(
        goodsRepository.save(targetGoods)
      );
    } else {
      // Goods 프로필 이미지 생성
      String requestPath
        = (String) multipartFileFacade.insertImage(ImageSort.GOODS, goodsImage);

      targetGoods.setGoodsName(dto.getGoodsName());
      targetGoods.setGoodsDescription(dto.getGoodsDescription());
      targetGoods.setGoodsPrice(dto.getGoodsPrice());
      targetGoods.setGoodsStock(dto.getGoodsStock());
      targetGoods.setGoodsImage(requestPath);

      // 반환
      return ResponseGoodsDto.fromEntity(
        goodsRepository.save(targetGoods)
      );
    }
  }

  // Delete - (owner) 상품 삭제
  public String deleteGoods(Long goodsId) {
    // 권한 확인
    shopAuth.checkShopAuthentication();

    // 삭제
    goodsRepository.deleteById(goodsId);

    return "done";
  }

  // Search - 쇼핑몰 상품 검색: 이름 & 가격 범위를 기준으로 상품 검색
  public List<SearchResponseGoodsDto> searchGoodsOfShop(Long shopId, String category, SearchDto dto) {
    List<SearchResponseGoodsDto> goodsDtos = new ArrayList<>();
    List<GoodsEntity> goodsEntities = new ArrayList<>();

    switch (category) {
      case "goodsName":
        goodsEntities = goodsRepository
          .findAllByShoppingMall_IdAndGoodsNameContaining(shopId, dto.getKeyword());
        break;

      case "goodsPrice":
        goodsEntities = goodsRepository
          .findAllByShoppingMall_IdAndGoodsPriceBetween(shopId, dto.getPriceFloor(), dto.getPriceCeil());
        break;
    }

    for (GoodsEntity entity : goodsEntities) {
      goodsDtos.add(SearchResponseGoodsDto.fromEntity(entity));
    }

    return goodsDtos;
  }
}
