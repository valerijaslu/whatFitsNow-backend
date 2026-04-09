package com.whatfitsnow.whatfitsnowbackend.me;

import com.whatfitsnow.whatfitsnowbackend.me.api.MeResponse;
import com.whatfitsnow.whatfitsnowbackend.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class MeController {

  @GetMapping
  public ResponseEntity<MeResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
    return ResponseEntity.ok(new MeResponse(principal.getUserId(), principal.getUsername()));
  }
}

