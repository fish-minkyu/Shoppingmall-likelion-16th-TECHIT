package com.example.shoppingmall.proposal.dto;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.proposal.entity.ProposalStatus;
import com.example.shoppingmall.used.entity.ItemEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalDto {
  private Long id;

  private UserEntity buyer;
  private UserEntity seller;

  private ProposalStatus proposalStatus;

  private ItemEntity item;
}
