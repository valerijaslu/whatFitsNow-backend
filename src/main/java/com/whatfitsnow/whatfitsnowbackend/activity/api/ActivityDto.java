package com.whatfitsnow.whatfitsnowbackend.activity.api;

import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import java.time.Instant;

public record ActivityDto(
    long id,
    long userId,
    String title,
    int minDurationMinutes,
    int maxDurationMinutes,
    EffortLevel effortLevel,
    LocationType locationType,
    SocialType socialType,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {
}

