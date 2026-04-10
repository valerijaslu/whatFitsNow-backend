package com.whatfitsnow.whatfitsnowbackend.activity;

import com.whatfitsnow.whatfitsnowbackend.activity.api.ActivityDto;

public final class ActivityMapper {
  private ActivityMapper() {
  }

  public static ActivityDto toDto(Activity a) {
    return new ActivityDto(
        a.getId(),
        a.getUserId(),
        a.getTitle(),
        a.getMinDurationMinutes(),
        a.getMaxDurationMinutes(),
        a.getEffortLevel(),
        a.getLocationType(),
        a.getSocialType(),
        a.isActive(),
        a.getCreatedAt(),
        a.getUpdatedAt()
    );
  }
}

