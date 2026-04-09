package com.whatfitsnow.whatfitsnowbackend.activity.api;

import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.WeatherCompatibility;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateActivityRequest(
    @NotBlank @Size(max = 120) String title,
    @Size(max = 1000) String description,
    @NotNull @Min(1) @Max(1440) Integer durationMinutes,
    @NotNull EffortLevel effortLevel,
    @NotNull @Min(1) @Max(5) Integer pleasureScore,
    @NotNull @Min(1) @Max(5) Integer satisfactionScore,
    @NotNull LocationType locationType,
    @NotNull SocialType socialType,
    @NotNull WeatherCompatibility weatherCompatibility,
    @NotNull @Min(1) @Max(5) Integer minEnergy,
    @NotNull @Min(1) @Max(5) Integer maxEnergy,
    @NotNull @Min(1) @Max(5) Integer minHealth,
    Boolean isActive,
    List<@NotBlank @Size(max = 40) String> tags
) {
}

