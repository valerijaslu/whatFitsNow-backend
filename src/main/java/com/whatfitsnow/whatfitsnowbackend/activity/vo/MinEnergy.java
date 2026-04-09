package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MinEnergy extends AbstractValueObject<Integer> {

  @Column(name = "min_energy", nullable = false)
  private Integer value;

  protected MinEnergy() {
    super();
  }

  private MinEnergy(Integer value) {
    super();
    this.value = value;
    validate();
  }

  public static MinEnergy of(Integer value) {
    return new MinEnergy(value);
  }

  @Override
  protected void validate() {
    if (value == null) {
      throw new InvalidValueException("minEnergy", "minEnergy is required");
    }
    if (value < 0 || value > 100) {
      throw new InvalidValueException("minEnergy", "minEnergy must be between 0 and 100");
    }
  }

  @Override
  public Integer value() {
    return value;
  }
}

