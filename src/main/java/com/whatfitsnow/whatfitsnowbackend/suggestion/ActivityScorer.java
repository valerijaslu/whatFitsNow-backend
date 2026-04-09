package com.whatfitsnow.whatfitsnowbackend.suggestion;

import com.whatfitsnow.whatfitsnowbackend.activity.Activity;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.WeatherCompatibility;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.PreferredLocationType;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.PreferredSocialType;
import com.whatfitsnow.whatfitsnowbackend.suggestion.api.SuggestionRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ActivityScorer {

  private ActivityScorer() {
  }

  public static Optional<ScoredActivity> scoreIfApplicable(Activity a, SuggestionRequest req) {
    List<String> reasons = new ArrayList<>();
    int score = 0;

    if (!fitsEnergy(a, req.energyLevel(), reasons)) {
      return Optional.empty();
    }
    score += 3;

    if (!fitsHealth(a, req.healthLevel(), reasons)) {
      return Optional.empty();
    }
    score += 3;

    if (!fitsWeather(a, req.currentWeather(), reasons)) {
      return Optional.empty();
    }
    score += 2;

    if (!fitsLocation(a, req.preferredLocationType(), reasons)) {
      return Optional.empty();
    }
    score += 2;

    if (!fitsSocial(a, req.preferredSocialType(), reasons)) {
      return Optional.empty();
    }
    score += 2;

    if (!fitsDuration(a, req.availableMinutes(), reasons)) {
      return Optional.empty();
    }
    score += 2;

    // Tie-breaker-ish boost: keep deterministic and transparent.
    score += a.getPleasureScore();
    score += a.getSatisfactionScore();
    reasons.add("high pleasure/satisfaction");

    return Optional.of(new ScoredActivity(a, score, reasons));
  }

  private static boolean fitsEnergy(Activity a, int energyLevel, List<String> reasons) {
    if (energyLevel < a.getMinEnergy() || energyLevel > a.getMaxEnergy()) {
      return false;
    }
    reasons.add("fits energy level");
    return true;
  }

  private static boolean fitsHealth(Activity a, int healthLevel, List<String> reasons) {
    if (healthLevel < a.getMinHealth()) {
      return false;
    }
    reasons.add("health compatible");
    return true;
  }

  private static boolean fitsWeather(Activity a, WeatherCompatibility current, List<String> reasons) {
    WeatherCompatibility needed = a.getWeatherCompatibility();
    if (needed == WeatherCompatibility.ANY || current == WeatherCompatibility.ANY || needed == current) {
      reasons.add(current == WeatherCompatibility.RAINY ? "good for rainy weather" : "weather compatible");
      return true;
    }
    return false;
  }

  private static boolean fitsLocation(Activity a, PreferredLocationType pref, List<String> reasons) {
    if (pref == PreferredLocationType.ANY) {
      reasons.add("location OK");
      return true;
    }
    LocationType activity = a.getLocationType();
    boolean ok = (activity == LocationType.BOTH)
        || (pref == PreferredLocationType.INDOOR && activity == LocationType.INDOOR)
        || (pref == PreferredLocationType.OUTDOOR && activity == LocationType.OUTDOOR);
    if (ok) {
      reasons.add("location compatible");
    }
    return ok;
  }

  private static boolean fitsSocial(Activity a, PreferredSocialType pref, List<String> reasons) {
    if (pref == PreferredSocialType.ANY) {
      reasons.add("social OK");
      return true;
    }
    SocialType activity = a.getSocialType();
    boolean ok = (activity == SocialType.BOTH)
        || (pref == PreferredSocialType.ALONE && activity == SocialType.ALONE)
        || (pref == PreferredSocialType.SOCIAL && activity == SocialType.SOCIAL);
    if (ok) {
      reasons.add("social compatible");
    }
    return ok;
  }

  private static boolean fitsDuration(Activity a, int availableMinutes, List<String> reasons) {
    if (a.getDurationMinutes() > availableMinutes) {
      return false;
    }
    reasons.add("short enough for available time");
    return true;
  }

  public record ScoredActivity(Activity activity, int score, List<String> reasons) {
  }
}

