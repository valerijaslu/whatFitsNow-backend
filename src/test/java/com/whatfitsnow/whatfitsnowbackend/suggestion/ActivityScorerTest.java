package com.whatfitsnow.whatfitsnowbackend.suggestion;

import com.whatfitsnow.whatfitsnowbackend.activity.Activity;
import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.WeatherCompatibility;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityDescription;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityTitle;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.DurationMinutes;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.EnergyRange;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.MinHealth;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.PleasureScore;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.SatisfactionScore;
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
        .effortLevel(EffortLevel.LOW)
        .pleasureScore(PleasureScore.of(3))
        .satisfactionScore(SatisfactionScore.of(3))
        .locationType(LocationType.INDOOR)
        .socialType(SocialType.ALONE)
        .weatherCompatibility(WeatherCompatibility.ANY)
        .energyRange(EnergyRange.of(4, 5))
        .minHealth(MinHealth.of(1))
        .active(true)
        .tags(null)
        .build();

    SuggestionRequest req = new SuggestionRequest(
        2,
        5,
        PreferredLocationType.ANY,
        PreferredSocialType.ANY,
        WeatherCompatibility.ANY,
        60
    );

    assertThat(ActivityScorer.scoreIfApplicable(a, req)).isEmpty();
  }

  @Test
  void higher_pleasure_and_satisfaction_rank_higher_given_same_fit() {
    User user = user();

    Activity low = base(user, "Low", 2, 2);
    Activity high = base(user, "High", 5, 5);

    SuggestionRequest req = new SuggestionRequest(
        3,
        3,
        PreferredLocationType.INDOOR,
        PreferredSocialType.ALONE,
        WeatherCompatibility.RAINY,
        30
    );

    var sLow = ActivityScorer.scoreIfApplicable(low, req).orElseThrow();
    var sHigh = ActivityScorer.scoreIfApplicable(high, req).orElseThrow();

    assertThat(sHigh.score()).isGreaterThan(sLow.score());
    assertThat(sHigh.reasons()).anyMatch(r -> r.contains("pleasure"));
  }

  private static Activity base(User user, String title, int pleasure, int satisfaction) {
    return Activity.builder()
        .user(user)
        .title(ActivityTitle.of(title))
        .description(ActivityDescription.ofNullable(null))
        .durationMinutes(DurationMinutes.of(20))
        .effortLevel(EffortLevel.LOW)
        .pleasureScore(PleasureScore.of(pleasure))
        .satisfactionScore(SatisfactionScore.of(satisfaction))
        .locationType(LocationType.BOTH)
        .socialType(SocialType.BOTH)
        .weatherCompatibility(WeatherCompatibility.ANY)
        .energyRange(EnergyRange.of(1, 5))
        .minHealth(MinHealth.of(1))
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

