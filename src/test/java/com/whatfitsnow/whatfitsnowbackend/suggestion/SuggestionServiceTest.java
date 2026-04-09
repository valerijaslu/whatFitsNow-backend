package com.whatfitsnow.whatfitsnowbackend.suggestion;

import com.whatfitsnow.whatfitsnowbackend.activity.Activity;
import com.whatfitsnow.whatfitsnowbackend.activity.ActivityRepository;
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
import com.whatfitsnow.whatfitsnowbackend.user.UserService;
import com.whatfitsnow.whatfitsnowbackend.user.vo.PasswordHash;
import com.whatfitsnow.whatfitsnowbackend.user.vo.UserEmail;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SuggestionServiceTest {

  @Autowired
  private SuggestionService suggestionService;

  @Autowired
  private ActivityRepository activityRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private com.whatfitsnow.whatfitsnowbackend.user.UserRepository userRepository;

  @Test
  void returns_top3_sorted_and_filters_inactive() {
    // Persist a user (reuse existing persistence stack)
    User user = userRepository.save(User.builder()
        .email(UserEmail.of("sugg@example.com"))
        .passwordHash(PasswordHash.of("hash"))
        .build());

    // 4 activities, one inactive, one too long, two good
    activityRepository.save(activity(user, "A", 10, true, 2, 2));   // valid
    activityRepository.save(activity(user, "B", 10, true, 5, 5));   // valid, higher scores
    activityRepository.save(activity(user, "C", 500, true, 5, 5));  // filtered by duration
    activityRepository.save(activity(user, "D", 10, false, 5, 5));  // filtered by inactive

    var req = new SuggestionRequest(
        3,
        3,
        PreferredLocationType.ANY,
        PreferredSocialType.ANY,
        WeatherCompatibility.ANY,
        30
    );

    var result = suggestionService.suggest(user.getId(), req);
    assertThat(result).hasSize(2);
    assertThat(result.getFirst().title()).isEqualTo("B");
    assertThat(result.getFirst().reasons()).isNotEmpty();
  }

  private static Activity activity(User user, String title, int minutes, boolean active, int pleasure, int satisfaction) {
    return Activity.builder()
        .user(user)
        .title(ActivityTitle.of(title))
        .description(ActivityDescription.ofNullable(null))
        .durationMinutes(DurationMinutes.of(minutes))
        .effortLevel(EffortLevel.LOW)
        .pleasureScore(PleasureScore.of(pleasure))
        .satisfactionScore(SatisfactionScore.of(satisfaction))
        .locationType(LocationType.BOTH)
        .socialType(SocialType.BOTH)
        .weatherCompatibility(WeatherCompatibility.ANY)
        .energyRange(EnergyRange.of(1, 5))
        .minHealth(MinHealth.of(1))
        .active(active)
        .tags(null)
        .build();
  }
}

