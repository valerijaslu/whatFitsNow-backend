package com.whatfitsnow.whatfitsnowbackend.suggestion.api;

import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.WeatherCompatibility;
import java.util.List;

public record SuggestedActivityResponse(
    long activityId,
    String title,
    String description,
    int durationMinutes,
    EffortLevel effortLevel,
    int pleasureScore,
    int satisfactionScore,
    LocationType locationType,
    SocialType socialType,
    WeatherCompatibility weatherCompatibility,
    int score,
    List<String> reasons
) {
}

