package com.example.shoppingmall.proposal;

import com.example.shoppingmall.auth.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.proposal.dto.ProposalDto;
import com.example.shoppingmall.proposal.entity.ProposalEntity;
import com.example.shoppingmall.proposal.entity.ProposalStatus;
import com.example.shoppingmall.proposal.repo.ProposalRepository;
import com.example.shoppingmall.used.entity.ItemEntity;
import com.example.shoppingmall.used.repo.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProposalService {
  private final ProposalRepository proposalRepository;
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final AuthenticationFacade auth;

  // Create
  public void createProposal(Long itemId)
  throws ResponseStatusException {
    try {
      // Buyer 정보 가져오기
      UserEntity buyer = auth.getAuth();

      // Item 정보 가져오기
      ItemEntity targetItem = itemRepository.findById(itemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // Seller 정보 가져오기
      Long sellerId = targetItem.getUser().getId();
      UserEntity seller = userRepository.findById(sellerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // Seller != Buyer 확인
      if (buyer.getId().equals(sellerId)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }

      // 이미 기존에 생성한 proposal이 있는지 확인

      Integer proposalCount = proposalRepository.countByBuyerAndItem(buyer, targetItem);

      if (proposalCount >= 1)
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

      // ProposalEntity 생성
      ProposalEntity proposal = ProposalEntity.builder()
        .seller(seller)
        .buyer(buyer)
        .item(targetItem)
        .proposalStatus(ProposalStatus.WAITING)
        .build();

      proposalRepository.save(proposal);
    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Read - readAll

  // Read - readOne

  // Update

  // Delete
}
