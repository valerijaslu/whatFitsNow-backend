package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DurationMinutes extends AbstractValueObject<Integer> {

  @Column(name = "duration_minutes", nullable = false)
  private Integer value;

  protected DurationMinutes() {
    super();
  }

  private DurationMinutes(Integer value) {
    super();
    this.value = value;
    validate();
  }

  public static DurationMinutes of(Integer value) {
    return new DurationMinutes(value);
  }

  @Override
  protected void validate() {
    if (value == null) {
      throw new InvalidValueException("durationMinutes", "Duration is required");
    }
    if (value < 1 || value > 24 * 60) {
      throw new InvalidValueException("durationMinutes", "Duration must be between 1 and 1440 minutes");
    }
  }

  @Override
  public Integer value() {
    return value;
  }
}

