package com.example.shoppingmall;

import com.example.shoppingmall.auth.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImageFacade {
  private final AuthenticationFacade auth;

  public void insertImage(GlobalStatus globalStatus, MultipartFile image) {
    // 접속 유저 정보를 가져온다.
    UserEntity targetUser = auth.getAuth();

    // sort에 따라 분기처리를 해준다.
    // 이 때, sort에 따라 넣어줄 변수 설정
    // profileDir, filename, requestPath
    switch (globalStatus) {
      case USER:

        break;
      case USED:
        break;
      case SHOP:
        break;
      case GOODS:
        break;
    }


  }
}
