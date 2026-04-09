package com.whatfitsnow.whatfitsnowbackend.suggestion;

import com.whatfitsnow.whatfitsnowbackend.activity.Activity;
import com.whatfitsnow.whatfitsnowbackend.activity.ActivityRepository;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.SuggestedActivityResponse;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.SuggestionRequest;
import com.whatfitsnow.whatfitsnowbackend.user.User;
import com.whatfitsnow.whatfitsnowbackend.user.UserService;
import java.util.Optional;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuggestionService {

  private final ActivityRepository activityRepository;
  private final UserService userService;

  public SuggestionService(ActivityRepository activityRepository, UserService userService) {
    this.activityRepository = activityRepository;
    this.userService = userService;
  }

  @Transactional(readOnly = true)
  public List<SuggestedActivityResponse> suggest(long userId, SuggestionRequest req) {
    User user = userService.requireUser(userId);
    List<Activity> activities = activityRepository.findAllByUserAndIsActiveTrue(user);

    return activities.stream()
        .map(a -> ActivityScorer.scoreIfApplicable(a, req))
        .flatMap(Optional::stream)
        .sorted(
            Comparator.<ActivityScorer.ScoredActivity>comparingInt(s -> s.score()).reversed()
                .thenComparing(s -> s.activity().getUpdatedAt(), Comparator.reverseOrder())
        )
        .limit(3)
        .map(s -> new SuggestedActivityResponse(
            s.activity().getId(),
            s.activity().getTitle(),
            s.activity().getDescription(),
            s.activity().getMinDurationMinutes(),
            s.activity().getMaxDurationMinutes(),
            s.activity().getEffortLevel(),
            s.activity().getLocationType(),
            s.activity().getSocialType(),
            s.activity().getWeatherCompatibility(),
            s.activity().getHealthCompatibility(),
            s.score(),
            s.reasons()
        ))
        .toList();
  }
}

