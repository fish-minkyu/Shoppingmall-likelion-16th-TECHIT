package com.example.shoppingmall.proposal.dto;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.proposal.entity.ProposalEntity;
import com.example.shoppingmall.proposal.entity.ProposalStatus;
import com.example.shoppingmall.used.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProposalDto {
  private Long id;

  private String buyerLoginId;
  private String sellerLoginId;

  private ProposalStatus proposalStatus;

  private String itemTitle;

  public static ProposalDto fromEntity(ProposalEntity entity) {
    return new ProposalDto(
      entity.getId(),
      entity.getBuyer().getLoginId(),
      entity.getSeller().getLoginId(),
      entity.getProposalStatus(),
      entity.getItem().getTitle()
    );
  }
}
