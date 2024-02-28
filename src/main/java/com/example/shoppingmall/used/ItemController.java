package com.example.shoppingmall.used;

import com.example.shoppingmall.used.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
  private final ItemService itemService;

  // Create
  @PostMapping("/enroll")
  public ItemDto createItem(
    @RequestBody ItemDto dto
    ) {
    return itemService.createItem(dto);
  }

  // Read

  // Update
  @PutMapping("/modifying/{id}")
  public ItemDto updateItem(
    @PathVariable("id") Long id,
    @RequestBody ItemDto dto
  ) {
    return itemService.updateItem(id, dto);
  }

  // Delete
  @DeleteMapping("/removing/{id}")
  public String deleteItem(
    @PathVariable("id") Long id
  ) {
    return itemService.deleteItem(id);
  }
}
