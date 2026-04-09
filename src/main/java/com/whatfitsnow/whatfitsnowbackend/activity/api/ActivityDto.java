package com.whatfitsnow.whatfitsnowbackend.activity.api;

import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.WeatherCompatibility;
import java.time.Instant;
import java.util.List;

public record ActivityDto(
    long id,
    long userId,
    String title,
    String description,
    int durationMinutes,
    EffortLevel effortLevel,
    int pleasureScore,
    int satisfactionScore,
    LocationType locationType,
    SocialType socialType,
    WeatherCompatibility weatherCompatibility,
    int minEnergy,
    int maxEnergy,
    int minHealth,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    List<String> tags
) {
}

