package com.whatfitsnow.whatfitsnowbackend.suggestion.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SuggestionRequest(
    @NotNull @Min(1) @Max(5) Integer energyLevel,
    @NotNull PreferredLocationType preferredLocationType,
    @NotNull PreferredSocialType preferredSocialType,
    @NotNull @Min(1) @Max(1440) Integer availableMinutes
) {
}

