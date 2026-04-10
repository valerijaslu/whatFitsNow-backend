package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractMultiValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.List;

@Embeddable
public class DurationRange extends AbstractMultiValueObject {

  @Column(name = "min_duration_minutes", nullable = false)
  private Integer min;

  @Column(name = "max_duration_minutes", nullable = false)
  private Integer max;

  protected DurationRange() {
  }

  private DurationRange(Integer min, Integer max) {
    this.min = min;
    this.max = max;
    validate();
  }

  public static DurationRange of(Integer min, Integer max) {
    return new DurationRange(min, max);
  }

  public int min() {
    return min;
  }

  public int max() {
    return max;
  }

  @Override
  protected void validate() {
    if (min == null) {
      throw new InvalidValueException("minDurationMinutes", "Min duration is required");
    }
    if (max == null) {
      throw new InvalidValueException("maxDurationMinutes", "Max duration is required");
    }
    if (min < 1 || min > 1440) {
      throw new InvalidValueException("minDurationMinutes", "Min duration must be between 1 and 1440 minutes");
    }
    if (max < 1 || max > 1440) {
      throw new InvalidValueException("maxDurationMinutes", "Max duration must be between 1 and 1440 minutes");
    }
    if (min > max) {
      throw new InvalidValueException("durationRange", "Min duration must be <= max duration");
    }
  }

  @Override
  protected List<Object> equalityComponents() {
    return List.of(min, max);
  }
}

