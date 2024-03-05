package com.example.shoppingmall.used;

import com.example.shoppingmall.ImageSort;
import com.example.shoppingmall.MultipartFileFacade;
import com.example.shoppingmall.auth.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.used.dto.RequestItemDto;
import com.example.shoppingmall.used.dto.ResponseItemDto;
import com.example.shoppingmall.used.entity.ItemEntity;
import com.example.shoppingmall.used.entity.ItemStatus;
import com.example.shoppingmall.used.repo.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsedService {
  private final ItemRepository itemRepository;
  private final AuthenticationFacade auth;
  private final MultipartFileFacade multipartFileFacade;

  // Create
  public ResponseItemDto createItem(RequestItemDto dto, MultipartFile usedImage) {
    try {
      UserEntity user = auth.getAuth();

      // 대표 이미지 유무에 따른 로직 분기 처리
      if (usedImage == null) {
        ItemEntity newItem = ItemEntity.builder()
          .title(dto.getTitle())
          .description(dto.getDescription())
          .price(dto.getPrice())
          .itemStatus(ItemStatus.SELLING)
          .user(user)
          .build();

        return ResponseItemDto.fromEntity(itemRepository.save(newItem));
      } else {
        // Used 메인 이미지 생성
        String requestPath
          = (String) multipartFileFacade.insertImage(ImageSort.USED, usedImage);

        ItemEntity newItem = ItemEntity.builder()
          .title(dto.getTitle())
          .description(dto.getDescription())
          .usedImage(requestPath)
          .price(dto.getPrice())
          .itemStatus(ItemStatus.SELLING)
          .user(user)
          .build();

        return ResponseItemDto.fromEntity(itemRepository.save(newItem));
      }
    } catch (Exception e) {
      log.error("error", e);
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Read
  public ResponseItemDto readOne(Long id) {
    ItemEntity item = itemRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return ResponseItemDto.fromEntity(item);
  }

  // Update - 제목, 설명, 최소가격, 대표 이미지
  public ResponseItemDto updateItem(
    Long usedId,
    RequestItemDto dto,
    MultipartFile usedImage
  ) {
    try {
      // 1. 해당 id에 맞는 item 가져오기
      ItemEntity targetItem = itemRepository.findById(usedId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // 2. 수정 요청자 정보 가져오기
      UserEntity user = auth.getAuth();

      // 3. 해당 수정 권한이 있는지 확인하기(등록자)
      // todo 관리자도 수정이 가능하도록 바꾸기
      if (!targetItem.getUser().getId().equals(user.getId()))
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

      // 4. 권한 확인 후, 수정 진행

      // 4-1. 수정할 이미지 저장
      String requestPath
        = (String) multipartFileFacade.insertImage(ImageSort.USED, usedImage);

      // 4-2. used 수정
      targetItem.setTitle(dto.getTitle());
      targetItem.setDescription(dto.getDescription());
      targetItem.setUsedImage(requestPath);
      targetItem.setPrice(dto.getPrice());

      return ResponseItemDto.fromEntity(itemRepository.save(targetItem));
    } catch (Exception e) {
      log.error("error", e);
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Update - 이미지 추가
  public ResponseItemDto updateImage(Long usedId, MultipartFile usedImage) {
    // 접속 유저 정보 가져오기
    UserEntity user = auth.getAuth();

    // 해당 아이템 가져오기
    ItemEntity targetUsed = itemRepository.findById(usedId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 권한 확인하기
    if (!targetUsed.getUser().getId().equals(user.getId()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    // 이미지 수정
    String requestPath
      = (String) multipartFileFacade.insertImage(ImageSort.USED, usedImage);

    targetUsed.setUsedImage(requestPath);

    // 저장 및 반환
    return ResponseItemDto.fromEntity(itemRepository.save(targetUsed));
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