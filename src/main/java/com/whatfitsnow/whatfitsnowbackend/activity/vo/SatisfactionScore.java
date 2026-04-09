package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class SatisfactionScore extends AbstractValueObject<Integer> {

  @Column(name = "satisfaction_score", nullable = false)
  private Integer value;

  protected SatisfactionScore() {
    super();
  }

  private SatisfactionScore(Integer value) {
    super();
    this.value = value;
    validate();
  }

  public static SatisfactionScore of(Integer value) {
    return new SatisfactionScore(value);
  }

  @Override
  protected void validate() {
    if (value == null) {
      throw new InvalidValueException("satisfactionScore", "Satisfaction score is required");
    }
    if (value < 1 || value > 5) {
      throw new InvalidValueException("satisfactionScore", "Satisfaction score must be between 1 and 5");
    }
  }

  @Override
  public Integer value() {
    return value;
  }
}

