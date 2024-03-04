package com.example.shoppingmall;

import com.example.shoppingmall.auth.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultipartFileFacade {
  private final AuthenticationFacade auth;

  public Object insertImage(ImageSort imageSort, MultipartFile image) {

    // 1. sort에 따라 분기처리를 해준다.
    // 이 때, sort에 따라 넣어줄 변수 설정
    // profileDir: 파일이 저장될 폴더 경로
    // format: 파일에 들어갈 분류명
    String profileDir = "";
    String format = "";
    switch (imageSort) {
      case USER:
        // 파일을 어디에 업로드 할건지 결정
        profileDir = "media/profile/";
        // 이미지 종류들을 구분하기 위해
        format = "_profile";
        break;
      case USED:
        // 파일을 어디에 업로드 할건지 결정
        profileDir = "media/item/";
        // 이미지 종류들을 구분하기 위해
        format = "_item";
        break;
      case GOODS:
        // 파일을 어디에 업로드 할건지 결정
        profileDir = "media/goods/";
        // 이미지 종류들을 구분하기 위해
        format = "_goods";
        break;
    }

    // (없다면) 폴더를 만들어야 한다.
    try{
      Files.createDirectories(Path.of(profileDir));
    } catch (IOException e) {
      log.error("err: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 3. 생성날짜 기준 filename 생성
    LocalDateTime now = LocalDateTime.now();
    String filename = now + format;

    // 4. extension 구하기
    String originalFilename = image.getOriginalFilename();
    String[] fileNameSplit = originalFilename.split("\\.");
    String extension = fileNameSplit[fileNameSplit.length -1];

    // 반환값
    String profileFilename = filename + "." + extension;
    // 저장되는 이미지 파일 경로
    String profilePath = profileDir + profileFilename;

    try {
      image.transferTo(Path.of(profilePath));
    } catch (IOException e) {
      log.error("err: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    String serverDomain = "localhost:8080";
    String requestPath = "";

    switch (imageSort) {
      case USER:
        // 접속 유저 정보를 가져온다.
        UserEntity targetUser = auth.getAuth();

        requestPath = String.format("%s/static/profile/%s", serverDomain, profileFilename);
        targetUser.setProfile(requestPath);
        return targetUser;

      case USED:
        return requestPath = String.format("%s/static/item/%s", serverDomain, profileFilename);

      case GOODS:
        return requestPath = String.format("%s/static/goods/%s", serverDomain, profileFilename);
    }

    return "";
  }
}
