package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PleasureScore extends AbstractValueObject<Integer> {

  @Column(name = "pleasure_score", nullable = false)
  private Integer value;

  protected PleasureScore() {
    super();
  }

  private PleasureScore(Integer value) {
    super();
    this.value = value;
    validate();
  }

  public static PleasureScore of(Integer value) {
    return new PleasureScore(value);
  }

  @Override
  protected void validate() {
    if (value == null) {
      throw new InvalidValueException("pleasureScore", "Pleasure score is required");
    }
    if (value < 1 || value > 5) {
      throw new InvalidValueException("pleasureScore", "Pleasure score must be between 1 and 5");
    }
  }

  @Override
  public Integer value() {
    return value;
  }
}

