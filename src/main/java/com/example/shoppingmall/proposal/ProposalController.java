package com.example.shoppingmall.proposal;

import com.example.shoppingmall.proposal.dto.ProposalDto;
import com.example.shoppingmall.proposal.entity.ProposalEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/used/{id}/proposal")
@RequiredArgsConstructor
public class ProposalController {
  private final ProposalService service;

  // Create: buyer가 seller에게 특정 아이템의 구메제안서 생성하기
  @PostMapping("/suggestion")
  public ProposalDto createProposal(
    @PathVariable("id") Long itemId
  ) {
    return service.createProposal(itemId);
  }

  // Read - readAll: seller가 특정 아이템의 전체 구매제안서 보기
  @GetMapping("/list")
  public List<ProposalDto> readAll(
    @PathVariable("id") Long itemId
  ) {
    return service.readAll(itemId);
  }

  // Read - readOne: buyer가 특정 아이템의 구매제안서 보기
  @GetMapping("/paper")
  public ProposalDto readOne(
    @PathVariable("id") Long itemId
  ) {
    return service.readOne(itemId);
  }

  // Update - accept: seller가 구매제안을 수락 또는 거절을 하는 것이다.
  @PutMapping("/{proposalId}/accepted")
  public ProposalDto updateSeller(
    @PathVariable("proposalId") Long proposalId,
    @RequestParam("accepted") Boolean accepted
  ) {

      return service.updateSeller(proposalId, accepted);
  }

  // update - confirmation: buyer가 구매확정을 할지 말지 결정한다.
  @PutMapping("/{proposalId}/confirmation")
  public ProposalDto updateBuyer(
    @PathVariable("id") Long itemId,
    @PathVariable("proposalId") Long proposalId,
    @RequestParam("confirmation") Boolean confirmation
  ) {
    return service.updateBuyer(itemId, proposalId, confirmation);
  }

  // Delete - buyer가 구매제안서를 취소한다
  @DeleteMapping("/{proposalId}/canceled")
  public String deleteBuyer(
    @PathVariable("proposalId") Long proposalId
  ) {
    return service.deleteBuyer(proposalId);
  }
}
