package com.whatfitsnow.whatfitsnowbackend.activity;

import com.whatfitsnow.whatfitsnowbackend.activity.api.ActivityDto;
import com.whatfitsnow.whatfitsnowbackend.tag.Tag;
import java.util.Comparator;
import java.util.List;

public final class ActivityMapper {
  private ActivityMapper() {
  }

  public static ActivityDto toDto(Activity a) {
    List<String> tags = a.getTags()
        .stream()
        .map(Tag::getName)
        .sorted(Comparator.naturalOrder())
        .toList();

    return new ActivityDto(
        a.getId(),
        a.getUserId(),
        a.getTitle(),
        a.getDescription(),
        a.getMinDurationMinutes(),
        a.getMaxDurationMinutes(),
        a.getEffortLevel(),
        a.getLocationType(),
        a.getSocialType(),
        a.getWeatherCompatibility(),
        a.getHealthCompatibility(),
        a.isActive(),
        a.getCreatedAt(),
        a.getUpdatedAt(),
        tags
    );
  }
}

