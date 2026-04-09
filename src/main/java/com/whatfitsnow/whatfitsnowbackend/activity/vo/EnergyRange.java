package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractMultiValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.List;

@Embeddable
public class EnergyRange extends AbstractMultiValueObject {

  @Column(name = "min_energy", nullable = false)
  private Integer min;

  @Column(name = "max_energy", nullable = false)
  private Integer max;

  protected EnergyRange() {
    super();
  }

  private EnergyRange(Integer min, Integer max) {
    super();
    this.min = min;
    this.max = max;
    validate();
  }

  public static EnergyRange of(Integer min, Integer max) {
    return new EnergyRange(min, max);
  }

  @Override
  protected void validate() {
    if (min == null) {
      throw new InvalidValueException("minEnergy", "minEnergy is required");
    }
    if (max == null) {
      throw new InvalidValueException("maxEnergy", "maxEnergy is required");
    }
    if (min < 1 || min > 5) {
      throw new InvalidValueException("minEnergy", "minEnergy must be between 1 and 5");
    }
    if (max < 1 || max > 5) {
      throw new InvalidValueException("maxEnergy", "maxEnergy must be between 1 and 5");
    }
    if (min > max) {
      throw new InvalidValueException("minEnergy", "minEnergy cannot be greater than maxEnergy");
    }
  }

  public int min() {
    return min;
  }

  public int max() {
    return max;
  }

  @Override
  protected List<Object> equalityComponents() {
    return List.of(min, max);
  }
}

