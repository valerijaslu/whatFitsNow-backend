package com.whatfitsnow.whatfitsnowbackend.user.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PasswordHash extends AbstractValueObject<String> {

  @Column(name = "password_hash", nullable = false)
  private String value;

  protected PasswordHash() {
    super();
  }

  private PasswordHash(String value) {
    super();
    this.value = value;
    validate();
  }

  public static PasswordHash of(String value) {
    return new PasswordHash(value);
  }

  @Override
  protected void validate() {
    if (value == null || value.isBlank()) {
      throw new InvalidValueException("password", "Password hash is required");
    }
    if (value.length() > 255) {
      throw new InvalidValueException("password", "Password hash is too long");
    }
  }

  @Override
  public String value() {
    return value;
  }
}

