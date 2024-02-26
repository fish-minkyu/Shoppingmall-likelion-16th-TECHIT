package com.example.shoppingmall.auth;

import com.example.shoppingmall.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByUserId(String userId);
}
