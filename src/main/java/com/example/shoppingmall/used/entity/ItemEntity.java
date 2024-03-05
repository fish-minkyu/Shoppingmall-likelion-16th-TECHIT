package com.example.shoppingmall.used.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.proposal.entity.ProposalEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(nullable = false)
  private String title;
  @Setter
  @Column(nullable = false)
  private String description;
  @Setter
  private String usedImage;
  @Setter
  @Column(nullable = false)
  private Integer price;

  @Setter
  @Enumerated(EnumType.STRING)
  private ItemStatus itemStatus;

  @ManyToOne
  private UserEntity user;

  @OneToMany(mappedBy = "item")
  private List<ProposalEntity> proposals = new ArrayList<>();
}