package com.example.shoppingmall.proposal.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.used.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProposalEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Enumerated(EnumType.STRING)
  private ProposalStatus proposalStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id")
  private UserEntity seller;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id")
  private UserEntity buyer;

  @ManyToOne(fetch = FetchType.LAZY)
  private ItemEntity item;
}