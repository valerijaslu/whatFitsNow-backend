package com.whatfitsnow.whatfitsnowbackend.suggestion.api;

import com.whatfitsnow.whatfitsnowbackend.activity.model.HealthCompatibility;
import com.whatfitsnow.whatfitsnowbackend.activity.model.WeatherCompatibility;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SuggestionRequest(
    @NotNull @Min(1) @Max(5) Integer energyLevel,
    @NotNull HealthCompatibility currentHealth,
    @NotNull PreferredLocationType preferredLocationType,
    @NotNull PreferredSocialType preferredSocialType,
    @NotNull WeatherCompatibility currentWeather,
    @NotNull @Min(1) @Max(1440) Integer availableMinutes
) {
}

