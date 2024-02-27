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
  public List<UserEntity> pending() {
    return adminService.pending();
  }

  @PutMapping("/judgement/{id}")
  public UserDto judgement(
    @PathVariable("id") Long id,
    @RequestParam("judgement") Boolean judgement
  ) {
    return adminService.judge(id, judgement);
  }
}
