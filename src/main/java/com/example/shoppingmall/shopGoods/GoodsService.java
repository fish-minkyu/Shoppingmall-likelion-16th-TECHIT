package com.example.shoppingmall.shopGoods;

import com.example.shoppingmall.shopGoods.repo.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsService {
  private GoodsRepository goodsRepository;


}
