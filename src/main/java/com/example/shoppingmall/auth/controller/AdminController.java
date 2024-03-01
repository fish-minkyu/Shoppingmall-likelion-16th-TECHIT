package com.example.shoppingmall.auth.controller;

import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
  private final AdminService adminService;

  // Read - SELECT * FROM userEntity WHERE authority = UserAuthority.PENDING
  @GetMapping("/businessPending")
  public List<UserEntity> pending() { //todo 반환을 UserEntity가 아닌 다른 걸로 바꿔줘야 함, password가 보이면 안됨
    return adminService.pending();
  }

  @PutMapping("/judgement/{id}")
  public UserDto judgement(
    @PathVariable("id") Long userId,
    @RequestParam("judgement") Boolean judgement
  ) {
    return adminService.judge(userId, judgement);
  }
}
