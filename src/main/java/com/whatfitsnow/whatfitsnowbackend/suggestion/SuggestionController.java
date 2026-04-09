package com.whatfitsnow.whatfitsnowbackend.suggestion;

import com.whatfitsnow.whatfitsnowbackend.security.UserPrincipal;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.SuggestedActivityResponse;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.SuggestionRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

  private final SuggestionService suggestionService;

  public SuggestionController(SuggestionService suggestionService) {
    this.suggestionService = suggestionService;
  }

  @PostMapping
  public ResponseEntity<List<SuggestedActivityResponse>> suggest(
      @AuthenticationPrincipal UserPrincipal principal,
      @Valid @RequestBody SuggestionRequest request
  ) {
    return ResponseEntity.ok(suggestionService.suggest(principal.getUserId(), request));
  }
}

