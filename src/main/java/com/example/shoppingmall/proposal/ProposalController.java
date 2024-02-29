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
@RequestMapping("/item/{id}/proposal")
@RequiredArgsConstructor
public class ProposalController {
  private final ProposalService service;

  // Create
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


  @GetMapping("/test")
  public String test() {
    return "success";
  }
}
