package com.example.shoppingmall.auth.entity;

import com.example.shoppingmall.used.entity.ItemEntity;
import com.example.shoppingmall.proposal.entity.ProposalEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "login_id")
  private String loginId;
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

  @Setter
  private String businessNumber;

  @Setter
  @Enumerated(EnumType.STRING)
  private UserAuthority authority;

  @OneToMany(mappedBy = "user")
  private List<ItemEntity> items = new ArrayList<>();

  @OneToMany(mappedBy = "seller")
  private List<ProposalEntity> sellers = new ArrayList<>();

  @OneToMany(mappedBy = "buyer")
  private List<ProposalEntity> buyers = new ArrayList<>();


  // 해당 필드 중 하나라도 null이면, false를 반환
  public boolean isNoNull() {
    return
      username != null &&
      nickname != null &&
      email != null &&
      ageRange != null &&
      phone != null &&
      profile != null;
  }
}