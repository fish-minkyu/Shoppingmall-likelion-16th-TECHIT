package com.example.shoppingmall.used;

import com.example.shoppingmall.auth.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.used.dto.ItemDto;
import com.example.shoppingmall.used.entity.ItemEntity;
import com.example.shoppingmall.used.entity.ItemStatus;
import com.example.shoppingmall.used.repo.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
  private final ItemRepository itemRepository;
  private final AuthenticationFacade auth;

  // Create
  public ItemDto createItem(ItemDto dto) {
    try {
      UserEntity user = auth.getAuth();

      log.info("UserEntity: {}", user);

      ItemEntity newItem = ItemEntity.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .postImage(dto.getPostImage())
        .price(dto.getPrice())
        .itemStatus(ItemStatus.SELLING)
        .user(user)
        .build();

      return ItemDto.fromEntity(itemRepository.save(newItem));
    } catch (Exception e) {
      log.error("error", e);
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Read
  public ItemDto readOne(Long id) {
    ItemEntity item = itemRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return ItemDto.fromEntity(item);
  }

  // Update
  public ItemDto updateItem(
    Long id,
    ItemDto dto
  ) {
    try {
      // 해당 id에 맞는 item 가져오기
      Optional<ItemEntity> optionalItem = itemRepository.findById(id);

      if (optionalItem.isEmpty())
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);

      ItemEntity targetEntity = optionalItem.get();

      // 수정 요청자 정보 가져오기
      UserEntity user = auth.getAuth();

      log.info("entity user id: {}", targetEntity.getUser().getId());
      log.info("user.getId: {}", user.getId());

      // 해당 수정 권한이 있는지 확인하기(등록자)
      // todo 관리자도 수정이 가능하도록 바꾸기
      if (!targetEntity.getUser().getId().equals(user.getId()))
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

      // 권한 확인 후, 수정 진행
      targetEntity.setTitle(dto.getTitle());
      targetEntity.setDescription(dto.getDescription());
      targetEntity.setPostImage(dto.getPostImage());
      targetEntity.setPrice(dto.getPrice());

      return ItemDto.fromEntity(itemRepository.save(targetEntity));
    } catch (Exception e) {
      log.error("error", e);
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Delete
  public String deleteItem(
    Long id
  ) {
    try {
      // 해당 id에 맞는 item 가져오기
      ItemEntity targetEntity = itemRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // 삭제 요청자 정보 가져오기
      UserEntity user = auth.getAuth();

      // 해당 수정 권한이 있는지 확인하기(등록자)
      // todo 관리자도 삭제가 가능하도록 바꾸기
      if (!targetEntity.getUser().getId().equals(user.getId()))
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

      // 권한 확인 후, 삭제 진행
      itemRepository.deleteById(id);

      return "done";
    } catch (Exception e) {
      log.error("error", e);
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}