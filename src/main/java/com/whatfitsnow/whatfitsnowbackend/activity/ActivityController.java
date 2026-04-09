package com.whatfitsnow.whatfitsnowbackend.activity;

import com.whatfitsnow.whatfitsnowbackend.activity.api.ActivityDto;
import com.whatfitsnow.whatfitsnowbackend.activity.api.CreateActivityRequest;
import com.whatfitsnow.whatfitsnowbackend.activity.api.UpdateActivityRequest;
import com.whatfitsnow.whatfitsnowbackend.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

  private final ActivityService activityService;

  public ActivityController(ActivityService activityService) {
    this.activityService = activityService;
  }

  @PostMapping
  public ResponseEntity<ActivityDto> create(
      @AuthenticationPrincipal UserPrincipal principal,
      @Valid @RequestBody CreateActivityRequest request
  ) {
    ActivityDto created = activityService.create(principal.getUserId(), request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ActivityDto> update(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable("id") long id,
      @Valid @RequestBody UpdateActivityRequest request
  ) {
    return ResponseEntity.ok(activityService.update(principal.getUserId(), id, request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ActivityDto> getById(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable("id") long id
  ) {
    return ResponseEntity.ok(activityService.getById(principal.getUserId(), id));
  }

  @GetMapping
  public ResponseEntity<List<ActivityDto>> list(@AuthenticationPrincipal UserPrincipal principal) {
    return ResponseEntity.ok(activityService.list(principal.getUserId()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable("id") long id
  ) {
    activityService.delete(principal.getUserId(), id);
    return ResponseEntity.noContent().build();
  }
}

