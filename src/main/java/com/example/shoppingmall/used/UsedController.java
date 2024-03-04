package com.example.shoppingmall.used;

import com.example.shoppingmall.used.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/used")
@RequiredArgsConstructor
public class UsedController {
  private final UsedService usedService;

  // Create
  @PostMapping("/enroll")
  public ItemDto createItem(
    @RequestBody ItemDto dto,
    @RequestParam("usedImage")MultipartFile usedImage
    ) {
    return usedService.createItem(dto, usedImage);
  }

  // Read
  @GetMapping("/{id}")
  public ItemDto readOne(
    @PathVariable("id") Long id
  ) {
    return usedService.readOne(id);
  }

  // Update
  @PutMapping("/modifying/{id}")
  public ItemDto updateItem(
    @PathVariable("id") Long id,
    @RequestBody ItemDto dto
  ) {
    return usedService.updateItem(id, dto);
  }

  // Delete
  @DeleteMapping("/removing/{id}")
  public String deleteItem(
    @PathVariable("id") Long id
  ) {
    return usedService.deleteItem(id);
  }
}