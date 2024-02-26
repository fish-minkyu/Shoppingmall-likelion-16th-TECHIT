package com.example.shoppingmall.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String userId;
  @Column(nullable = false)
  private String password;

  @Setter
  private String username;
  @Setter
  private String nickname;
  @Setter
  private String email;
  @Setter
  private String ageRange;
  @Setter
  private String phone;
  @Setter
  private String profile;

  @Enumerated(EnumType.STRING)
  @Setter
  private UserAuthority authority;
}
