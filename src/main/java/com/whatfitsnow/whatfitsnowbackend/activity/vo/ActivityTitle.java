package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ActivityTitle extends AbstractValueObject<String> {

  @Column(name = "title", nullable = false, length = 120)
  private String value;

  protected ActivityTitle() {
    super();
  }

  private ActivityTitle(String value) {
    super();
    this.value = value;
    validate();
  }

  public static ActivityTitle of(String raw) {
    if (raw == null) {
      throw new InvalidValueException("title", "Title is required");
    }
    return new ActivityTitle(raw.trim());
  }

  @Override
  protected void validate() {
    if (value == null || value.isBlank()) {
      throw new InvalidValueException("title", "Title is required");
    }
    if (value.length() > 120) {
      throw new InvalidValueException("title", "Title is too long");
    }
  }

  @Override
  public String value() {
    return value;
  }
}

