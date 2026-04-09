package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MaxEnergy extends AbstractValueObject<Integer> {

  @Column(name = "max_energy", nullable = false)
  private Integer value;

  protected MaxEnergy() {
    super();
  }

  private MaxEnergy(Integer value) {
    super();
    this.value = value;
    validate();
  }

  public static MaxEnergy of(Integer value) {
    return new MaxEnergy(value);
  }

  @Override
  protected void validate() {
    if (value == null) {
      throw new InvalidValueException("maxEnergy", "maxEnergy is required");
    }
    if (value < 0 || value > 100) {
      throw new InvalidValueException("maxEnergy", "maxEnergy must be between 0 and 100");
    }
  }

  @Override
  public Integer value() {
    return value;
  }
}

