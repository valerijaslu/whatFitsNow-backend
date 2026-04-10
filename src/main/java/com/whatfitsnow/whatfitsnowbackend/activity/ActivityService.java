package com.whatfitsnow.whatfitsnowbackend.activity;

import com.whatfitsnow.whatfitsnowbackend.activity.api.ActivityDto;
import com.whatfitsnow.whatfitsnowbackend.activity.api.CreateActivityRequest;
import com.whatfitsnow.whatfitsnowbackend.activity.api.UpdateActivityRequest;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityTitle;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.DurationRange;
import com.whatfitsnow.whatfitsnowbackend.common.exception.NotFoundException;
import com.whatfitsnow.whatfitsnowbackend.user.User;
import com.whatfitsnow.whatfitsnowbackend.user.UserService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityService {

  private final ActivityRepository activityRepository;
  private final UserService userService;

  public ActivityService(ActivityRepository activityRepository, UserService userService) {
    this.activityRepository = activityRepository;
    this.userService = userService;
  }

  @Transactional
  public ActivityDto create(long userId, CreateActivityRequest req) {
    User user = userService.requireUser(userId);

    Activity activity = Activity.builder()
        .user(user)
        .title(ActivityTitle.of(req.title()))
        .durationRange(DurationRange.of(req.minDurationMinutes(), req.maxDurationMinutes()))
        .effortLevel(req.effortLevel())
        .locationType(req.locationType())
        .socialType(req.socialType())
        .active(req.isActive() == null || req.isActive())
        .build();

    Activity saved = activityRepository.save(activity);
    return ActivityMapper.toDto(saved);
  }

  @Transactional
  public ActivityDto update(long userId, long activityId, UpdateActivityRequest req) {
    User user = userService.requireUser(userId);
    Activity activity = activityRepository.findByIdAndUser(activityId, user)
        .orElseThrow(() -> new NotFoundException("Activity not found"));

    Activity.builder(activity)
        .title(ActivityTitle.of(req.title()))
        .durationRange(DurationRange.of(req.minDurationMinutes(), req.maxDurationMinutes()))
        .effortLevel(req.effortLevel())
        .locationType(req.locationType())
        .socialType(req.socialType())
        .active(req.isActive())
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
}

