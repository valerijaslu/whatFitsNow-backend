package com.whatfitsnow.whatfitsnowbackend.activity.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ActivityDescription extends AbstractValueObject<String> {

  @Column(name = "description", length = 1000)
  private String value;

  protected ActivityDescription() {
    super();
  }

  private ActivityDescription(String value) {
    super();
    this.value = value;
    validate();
  }

  public static ActivityDescription ofNullable(String raw) {
    if (raw == null) {
      return new ActivityDescription(null);
    }
    String trimmed = raw.trim();
    if (trimmed.isEmpty()) {
      return new ActivityDescription(null);
    }
    return new ActivityDescription(trimmed);
  }

  @Override
  protected void validate() {
    if (value != null && value.length() > 1000) {
      throw new InvalidValueException("description", "Description is too long");
    }
  }

  @Override
  public String value() {
    return value;
  }
}

