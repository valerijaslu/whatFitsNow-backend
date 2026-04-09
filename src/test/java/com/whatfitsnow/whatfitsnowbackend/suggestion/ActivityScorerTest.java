package com.whatfitsnow.whatfitsnowbackend.suggestion;

import com.whatfitsnow.whatfitsnowbackend.activity.Activity;
import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.HealthCompatibility;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.WeatherCompatibility;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityDescription;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityTitle;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.DurationMinutes;
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
        .description(ActivityDescription.ofNullable(null))
        .durationMinutes(DurationMinutes.of(10))
        .effortLevel(EffortLevel.HIGH)
        .locationType(LocationType.INDOOR)
        .socialType(SocialType.ALONE)
        .weatherCompatibility(WeatherCompatibility.ANY)
        .healthCompatibility(HealthCompatibility.ANY)
        .active(true)
        .tags(null)
        .build();

    SuggestionRequest req = new SuggestionRequest(
        2,
        HealthCompatibility.ANY,
        PreferredLocationType.ANY,
        PreferredSocialType.ANY,
        WeatherCompatibility.ANY,
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
        HealthCompatibility.ANY,
        PreferredLocationType.INDOOR,
        PreferredSocialType.ALONE,
        WeatherCompatibility.RAINY,
        30
    );

    var scored = ActivityScorer.scoreIfApplicable(a, req).orElseThrow();
    assertThat(scored.reasons()).isNotEmpty();
  }

  private static Activity base(User user, String title) {
    return Activity.builder()
        .user(user)
        .title(ActivityTitle.of(title))
        .description(ActivityDescription.ofNullable(null))
        .durationMinutes(DurationMinutes.of(20))
        .effortLevel(EffortLevel.LOW)
        .locationType(LocationType.BOTH)
        .socialType(SocialType.BOTH)
        .weatherCompatibility(WeatherCompatibility.ANY)
        .healthCompatibility(HealthCompatibility.ANY)
        .active(true)
        .tags(null)
        .build();
  }

  private static User user() {
    return User.builder()
        .email(UserEmail.of("test@example.com"))
        .passwordHash(PasswordHash.of("hash"))
        .build();
  }
}

