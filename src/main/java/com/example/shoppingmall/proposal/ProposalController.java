package com.example.shoppingmall.proposal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/item/{id}/proposal")
@RequiredArgsConstructor
public class ProposalController {
  private final ProposalService service;

  @PostMapping("/suggestion")
  public void createProposal(
    @PathVariable("id") Long itemId
  ) {
    service.createProposal(itemId);
  }

  @GetMapping("/test")
  public String test() {
    return "success";
  }
}
