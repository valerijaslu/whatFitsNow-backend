package com.whatfitsnow.whatfitsnowbackend.activity;

import com.whatfitsnow.whatfitsnowbackend.activity.api.ActivityDto;
import com.whatfitsnow.whatfitsnowbackend.activity.api.CreateActivityRequest;
import com.whatfitsnow.whatfitsnowbackend.activity.api.UpdateActivityRequest;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityDescription;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityTitle;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.DurationMinutes;
import com.whatfitsnow.whatfitsnowbackend.common.exception.NotFoundException;
import com.whatfitsnow.whatfitsnowbackend.tag.Tag;
import com.whatfitsnow.whatfitsnowbackend.tag.TagRepository;
import com.whatfitsnow.whatfitsnowbackend.tag.vo.TagName;
import com.whatfitsnow.whatfitsnowbackend.user.User;
import com.whatfitsnow.whatfitsnowbackend.user.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityService {

  private final ActivityRepository activityRepository;
  private final TagRepository tagRepository;
  private final UserService userService;

  public ActivityService(ActivityRepository activityRepository, TagRepository tagRepository, UserService userService) {
    this.activityRepository = activityRepository;
    this.tagRepository = tagRepository;
    this.userService = userService;
  }

  @Transactional
  public ActivityDto create(long userId, CreateActivityRequest req) {
    User user = userService.requireUser(userId);
    Set<Tag> tags = resolveTags(user, req.tags());

    Activity activity = Activity.builder()
        .user(user)
        .title(ActivityTitle.of(req.title()))
        .description(ActivityDescription.ofNullable(req.description()))
        .durationMinutes(DurationMinutes.of(req.durationMinutes()))
        .effortLevel(req.effortLevel())
        .locationType(req.locationType())
        .socialType(req.socialType())
        .weatherCompatibility(req.weatherCompatibility())
        .healthCompatibility(req.healthCompatibility())
        .active(req.isActive() == null || req.isActive())
        .tags(tags)
        .build();

    Activity saved = activityRepository.save(activity);
    return ActivityMapper.toDto(saved);
  }

  @Transactional
  public ActivityDto update(long userId, long activityId, UpdateActivityRequest req) {
    User user = userService.requireUser(userId);
    Activity activity = activityRepository.findByIdAndUser(activityId, user)
        .orElseThrow(() -> new NotFoundException("Activity not found"));

    Set<Tag> tags = resolveTags(user, req.tags());

    Activity.builder(activity)
        .title(ActivityTitle.of(req.title()))
        .description(ActivityDescription.ofNullable(req.description()))
        .durationMinutes(DurationMinutes.of(req.durationMinutes()))
        .effortLevel(req.effortLevel())
        .locationType(req.locationType())
        .socialType(req.socialType())
        .weatherCompatibility(req.weatherCompatibility())
        .healthCompatibility(req.healthCompatibility())
        .active(req.isActive())
        .tags(tags)
        .build();

    return ActivityMapper.toDto(activity);
  }

  @Transactional(readOnly = true)
  public ActivityDto getById(long userId, long activityId) {
    User user = userService.requireUser(userId);
    Activity activity = activityRepository.findByIdAndUser(activityId, user)
        .orElseThrow(() -> new NotFoundException("Activity not found"));
    return ActivityMapper.toDto(activity);
  }

  @Transactional(readOnly = true)
  public List<ActivityDto> list(long userId) {
    User user = userService.requireUser(userId);
    return activityRepository.findAllByUserOrderByUpdatedAtDesc(user)
        .stream()
        .map(ActivityMapper::toDto)
        .toList();
  }

  @Transactional
  public void delete(long userId, long activityId) {
    User user = userService.requireUser(userId);
    Activity activity = activityRepository.findByIdAndUser(activityId, user)
        .orElseThrow(() -> new NotFoundException("Activity not found"));
    activityRepository.delete(activity);
  }

  private Set<Tag> resolveTags(User user, List<String> rawNames) {
    if (rawNames == null || rawNames.isEmpty()) {
      return Set.of();
    }

    Set<Tag> tags = new HashSet<>();
    for (String raw : rawNames) {
      TagName name = TagName.of(raw);
      Tag tag = tagRepository.findByUserAndName(user, name)
          .orElseGet(() -> tagRepository.save(Tag.builder().user(user).name(name).build()));
      tags.add(tag);
    }
    return tags;
  }
}

