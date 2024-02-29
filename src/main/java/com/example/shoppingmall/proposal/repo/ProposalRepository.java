package com.example.shoppingmall.proposal.repo;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.proposal.entity.ProposalEntity;
import com.example.shoppingmall.used.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<ProposalEntity, Long> {
  Integer countByBuyerAndItem(UserEntity buyer, ItemEntity item);
}
