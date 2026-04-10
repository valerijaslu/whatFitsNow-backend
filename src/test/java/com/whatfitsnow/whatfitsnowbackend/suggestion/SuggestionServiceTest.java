package com.whatfitsnow.whatfitsnowbackend.suggestion;

import com.whatfitsnow.whatfitsnowbackend.activity.Activity;
import com.whatfitsnow.whatfitsnowbackend.activity.ActivityRepository;
import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityTitle;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.DurationRange;
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
    activityRepository.save(activity(user, "A", 10, true));   // valid
    activityRepository.save(activity(user, "B", 10, true));   // valid
    activityRepository.save(activity(user, "C", 500, true));  // filtered by duration
    activityRepository.save(activity(user, "D", 10, false));  // filtered by inactive

    var req = new SuggestionRequest(
        3,
        PreferredLocationType.ANY,
        PreferredSocialType.ANY,
        30
    );

    var result = suggestionService.suggest(user.getId(), req);
    assertThat(result).hasSize(2);
    assertThat(result.getFirst().title()).isIn("A", "B");
    assertThat(result.getFirst().reasons()).isNotEmpty();
  }

  @Test
  void returns_empty_when_no_activity_matches() {
    User user = userRepository.save(User.builder()
        .email(UserEmail.of("nomatch@example.com"))
        .passwordHash(PasswordHash.of("hash"))
        .build());

    // activity requires more energy than request provides
    activityRepository.save(Activity.builder()
        .user(user)
        .title(ActivityTitle.of("Hard thing"))
        .durationRange(DurationRange.of(10, 60))
        .effortLevel(EffortLevel.HIGH)
        .locationType(LocationType.INDOOR)
        .socialType(SocialType.ALONE)
        .active(true)
        .build());

    var req = new SuggestionRequest(
        1,
        PreferredLocationType.ANY,
        PreferredSocialType.ANY,
        30
    );

    assertThat(suggestionService.suggest(user.getId(), req)).isEmpty();
  }

  private static Activity activity(User user, String title, int minutes, boolean active) {
    return Activity.builder()
        .user(user)
        .title(ActivityTitle.of(title))
        .durationRange(DurationRange.of(minutes, minutes))
        .effortLevel(EffortLevel.MEDIUM)
        .locationType(LocationType.BOTH)
        .socialType(SocialType.BOTH)
        .active(active)
        .build();
  }
}

