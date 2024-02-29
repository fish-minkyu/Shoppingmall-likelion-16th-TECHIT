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
  @GetMapping("/{id}")
  public ItemDto readOne(
    @PathVariable("id") Long id
  ) {
    return itemService.readOne(id);
  }

  // Update
  @PutMapping("/{id}/modifying")
  public ItemDto updateItem(
    @PathVariable("id") Long id,
    @RequestBody ItemDto dto
  ) {
    return itemService.updateItem(id, dto);
  }

  // Delete
  @DeleteMapping("/{id}/removing")
  public String deleteItem(
    @PathVariable("id") Long id
  ) {
    return itemService.deleteItem(id);
  }
}