package com.whatfitsnow.whatfitsnowbackend.tag.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class TagName extends AbstractValueObject<String> {

  @Column(name = "name", nullable = false, length = 40)
  private String value;

  protected TagName() {
    super();
  }

  private TagName(String value) {
    super();
    this.value = value;
    validate();
  }

  public static TagName of(String raw) {
    if (raw == null) {
      throw new InvalidValueException("name", "Tag name is required");
    }
    String normalized = raw.trim();
    return new TagName(normalized);
  }

  @Override
  protected void validate() {
    if (value == null || value.isBlank()) {
      throw new InvalidValueException("name", "Tag name is required");
    }
    if (value.length() > 40) {
      throw new InvalidValueException("name", "Tag name is too long");
    }
  }

  @Override
  public String value() {
    return value;
  }
}

