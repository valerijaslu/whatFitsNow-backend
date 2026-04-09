package com.whatfitsnow.whatfitsnowbackend.user.vo;

import com.whatfitsnow.whatfitsnowbackend.common.valueobject.AbstractValueObject;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public class UserEmail extends AbstractValueObject<String> {

  private static final Pattern BASIC_EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  @Column(name = "email", nullable = false, unique = true)
  private String value;

  protected UserEmail() {
    super();
  }

  private UserEmail(String value) {
    super();
    this.value = value;
    validate();
  }

  public static UserEmail of(String raw) {
    if (raw == null) {
      throw new InvalidValueException("email", "Email is required");
    }
    String normalized = raw.trim().toLowerCase();
    return new UserEmail(normalized);
  }

  @Override
  protected void validate() {
    String v = value;
    if (v == null || v.isBlank()) {
      throw new InvalidValueException("email", "Email is required");
    }
    if (v.length() > 255) {
      throw new InvalidValueException("email", "Email is too long");
    }
    if (!BASIC_EMAIL.matcher(v).matches()) {
      throw new InvalidValueException("email", "Email is invalid");
    }
  }

  @Override
  public String value() {
    return value;
  }
}

