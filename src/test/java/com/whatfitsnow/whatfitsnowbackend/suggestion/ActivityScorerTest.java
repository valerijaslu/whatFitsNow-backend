package com.whatfitsnow.whatfitsnowbackend.suggestion;

import com.whatfitsnow.whatfitsnowbackend.activity.Activity;
import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityTitle;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.DurationRange;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.PreferredLocationType;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.PreferredSocialType;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.SuggestionRequest;
import com.whatfitsnow.whatfitsnowbackend.user.User;
import com.whatfitsnow.whatfitsnowbackend.user.vo.PasswordHash;
import com.whatfitsnow.whatfitsnowbackend.user.vo.UserEmail;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityScorerTest {

  @Test
  void filters_out_activity_that_does_not_fit_energy() {
    User user = user();

    Activity a = Activity.builder()
        .user(user)
        .title(ActivityTitle.of("A"))
        .durationRange(DurationRange.of(10, 60))
        .effortLevel(EffortLevel.HIGH)
        .locationType(LocationType.INDOOR)
        .socialType(SocialType.ALONE)
        .active(true)
        .build();

    SuggestionRequest req = new SuggestionRequest(
        2,
        PreferredLocationType.ANY,
        PreferredSocialType.ANY,
        60
    );

    assertThat(ActivityScorer.scoreIfApplicable(a, req)).isEmpty();
  }

  @Test
  void score_if_applicable_returns_reasons_for_matching_activity() {
    User user = user();

    Activity a = base(user, "A");

    SuggestionRequest req = new SuggestionRequest(
        2,
        PreferredLocationType.INDOOR,
        PreferredSocialType.ALONE,
        30
    );

    var scored = ActivityScorer.scoreIfApplicable(a, req).orElseThrow();
    assertThat(scored.reasons()).isNotEmpty();
  }

  private static Activity base(User user, String title) {
    return Activity.builder()
        .user(user)
        .title(ActivityTitle.of(title))
        .durationRange(DurationRange.of(20, 20))
        .effortLevel(EffortLevel.LOW)
        .locationType(LocationType.BOTH)
        .socialType(SocialType.BOTH)
        .active(true)
        .build();
  }

  private static User user() {
    return User.builder()
        .email(UserEmail.of("test@example.com"))
        .passwordHash(PasswordHash.of("hash"))
        .build();
  }
}

