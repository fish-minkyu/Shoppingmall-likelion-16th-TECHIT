package com.example.shoppingmall.used;

import com.example.shoppingmall.used.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
  private final ItemService itemService;

  @PostMapping("/enroll")
  public ItemDto createItem(
    @RequestBody ItemDto dto
    ) {
    return itemService.createItem(dto);
  }
}
