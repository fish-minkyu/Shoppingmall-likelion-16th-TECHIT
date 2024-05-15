package com.example.shoppingmall.product.repo;

import com.example.shoppingmall.proposal.entity.ProposalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<ProposalEntity, Long> {
}
