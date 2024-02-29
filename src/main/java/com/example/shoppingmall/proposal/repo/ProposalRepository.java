package com.example.shoppingmall.proposal.repo;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.proposal.entity.ProposalEntity;
import com.example.shoppingmall.used.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<ProposalEntity, Long> {
  Integer countByBuyerIdAndItemId(Long buyerId, Long itemId);

  List<ProposalEntity> findAllBySellerIdAndItemId(Long sellerId, Long itemId);

  ProposalEntity findByBuyerIdAndItemId(Long buyerId, Long itemId);
}
