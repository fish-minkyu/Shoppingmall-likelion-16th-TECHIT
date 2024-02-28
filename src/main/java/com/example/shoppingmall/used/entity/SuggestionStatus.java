package com.example.shoppingmall.used.entity;

public enum SuggestionStatus {
  WAITING("대기"),
  ACCEPTED("수락"),
  DENIED("거절");

  private String suggestionStatus;

  SuggestionStatus(String suggestionStatus) {
    this.suggestionStatus = suggestionStatus;
  }
}
