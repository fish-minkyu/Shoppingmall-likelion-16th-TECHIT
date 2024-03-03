package com.example.shoppingmall.proposal;

import com.example.shoppingmall.auth.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.proposal.dto.ProposalDto;
import com.example.shoppingmall.proposal.entity.ProposalEntity;
import com.example.shoppingmall.proposal.entity.ProposalStatus;
import com.example.shoppingmall.proposal.repo.ProposalRepository;
import com.example.shoppingmall.used.entity.ItemEntity;
import com.example.shoppingmall.used.entity.ItemStatus;
import com.example.shoppingmall.used.repo.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProposalService {
  private final ProposalRepository proposalRepository;
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final AuthenticationFacade auth;

  // Create
  public ProposalDto createProposal(Long itemId)
  throws ResponseStatusException {
    try {
      // buyer 정보 가져오기
      UserEntity buyer = auth.getAuth();

      // item 정보 가져오기
      ItemEntity targetItem = itemRepository.findById(itemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // seller 정보 가져오기
      Long sellerId = targetItem.getUser().getId();
      UserEntity seller = userRepository.findById(sellerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // seller != buyer 확인
      if (buyer.getId().equals(sellerId)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }

      // 이미 기존에 생성한 proposal이 있는지 확인
      Integer proposalCount = proposalRepository.countByBuyerIdAndItemId(buyer.getId(), targetItem.getId());

      if (proposalCount >= 1)
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

      // ProposalEntity 생성
      ProposalEntity proposal = ProposalEntity.builder()
        .seller(seller)
        .buyer(buyer)
        .item(targetItem)
        .proposalStatus(ProposalStatus.WAITING)
        .build();

      return ProposalDto.fromEntity(proposalRepository.save(proposal));
    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Read - readAll: seller가 특정 아이템의 전체 구매제안서 보기
  public List<ProposalDto> readAll(Long itemId) {
    // seller 정보 가져오기
    UserEntity seller = auth.getAuth();

    // proposalDtoList 선언
    List<ProposalDto> proposalDtoList = new ArrayList<>();

    // proposalEntityList 찾기
    List<ProposalEntity> proposalEntityList
      = proposalRepository.findAllBySellerIdAndItemId(seller.getId(),itemId);

    // proposalEntityList -> proposalDtoList
    for (ProposalEntity entity : proposalEntityList) {
      proposalDtoList.add(
        ProposalDto.fromEntity(entity)
      );
    }

    return proposalDtoList;
  }

  // Read - readOne: buyer가 특정 아이템의 구매제안서 보기
  public ProposalDto readOne(Long itemId) {
    try {
      // buyer 정보 가져오기
      UserEntity buyer = auth.getAuth();

      // SELECT * FROM Proposal p WHERE p.buyer_id = buyer.getId() AND p.item_id = itemId
      ProposalEntity targetEntity = proposalRepository.findByBuyerIdAndItemId(buyer.getId(), itemId);
      return ProposalDto.fromEntity(targetEntity);
    } catch (Exception e) {
      log.error("error: {}", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Update - accept: seller가 구매제안을 수락 또는 거절을 하는 것이다.
  public ProposalDto updateSeller(Long proposalId, Boolean accepted) {
    try {
      // seller를 가져온다.
      UserEntity seller = auth.getAuth();

      // 구매제안을 가져온다.
      ProposalEntity targetProposal = proposalRepository.findById(proposalId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      //todo seller 정보와 구매제안의 seller를 비교한다.

      // accepted에 따라 로직을 분기처리한다.
      if (accepted) {
        targetProposal.setProposalStatus(ProposalStatus.ACCEPTED);
      } else {
        targetProposal.setProposalStatus(ProposalStatus.DENIED);
      }

      return ProposalDto.fromEntity(proposalRepository.save(targetProposal));
    } catch (Exception e) {
      log.error("err: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // update - confirm: buyer가 구매확정을 확정하는 것이다.
  public ProposalDto updateBuyer(Long itemId, Long proposalId, Boolean confirmation) {
    try {
      // buyer 정보 가져오기
      UserEntity buyer = auth.getAuth();

      // 구매제안을 가져온다.
      ProposalEntity targetProposal = proposalRepository.findById(proposalId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      //todo buyer 정보와 구매제안의 buyer를 비교한다.

      // 해당 아이템을 가져온다.
      ItemEntity targetItem = itemRepository.findById(itemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

      // confirmation에 따라 로직을 분기처리한다.
      if (confirmation) {
        // 구매 확정
        targetProposal.setProposalStatus(ProposalStatus.CONFIRMATION);
        // 아이템 품절
        targetItem.setItemStatus(ItemStatus.SOLD);

        // item save
        itemRepository.save(targetItem);
        // proposal save
        return ProposalDto.fromEntity(proposalRepository.save(targetProposal));
      } else {
       // 구매 거절
       targetProposal.setProposalStatus(ProposalStatus.DENIED);
        return ProposalDto.fromEntity(proposalRepository.save(targetProposal));
      }
    } catch (Exception e) {
      log.error("err: {}", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Delete - buyer가 구매제안서를 취소한다
  public String deleteBuyer(Long proposalId) {
    // buyer 정보를 가져온다.
    UserEntity buyer = auth.getAuth();

    // 해당 구매제안서를 가져온다.
    ProposalEntity targetProposal = proposalRepository.findById(proposalId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 요청 buyer와 구매제안서의 buyer를 비교한다.
    if (!buyer.getId().equals(targetProposal.getBuyer().getId()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    proposalRepository.deleteById(proposalId);
    return "done";
  }

}
