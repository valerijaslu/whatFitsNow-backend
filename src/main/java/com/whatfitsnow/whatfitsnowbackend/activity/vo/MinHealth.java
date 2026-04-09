package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MinHealth extends AbstractValueObject<Integer> {

  @Column(name = "min_health", nullable = false)
  private Integer value;

  protected MinHealth() {
    super();
  }

  private MinHealth(Integer value) {
    super();
    this.value = value;
    validate();
  }

  public static MinHealth of(Integer value) {
    return new MinHealth(value);
  }

  @Override
  protected void validate() {
    if (value == null) {
      throw new InvalidValueException("minHealth", "minHealth is required");
    }
    if (value < 1 || value > 5) {
      throw new InvalidValueException("minHealth", "minHealth must be between 1 and 5");
    }
  }

  @Override
  public Integer value() {
    return value;
  }
}

