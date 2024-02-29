package com.example.shoppingmall.auth.repo;

import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByLoginId(String loginId);

  Optional<UserEntity> findByLoginId(String loginId);

  List<UserEntity> findAllByAuthority(UserAuthority authority);
}