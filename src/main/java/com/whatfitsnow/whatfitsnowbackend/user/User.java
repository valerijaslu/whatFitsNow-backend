package com.whatfitsnow.whatfitsnowbackend.user;

import com.whatfitsnow.whatfitsnowbackend.common.builder.AbstractBuilder;
import com.whatfitsnow.whatfitsnowbackend.common.persistence.AbstractEntity;
import com.whatfitsnow.whatfitsnowbackend.user.vo.PasswordHash;
import com.whatfitsnow.whatfitsnowbackend.user.vo.UserEmail;
import jakarta.persistence.Entity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {

  @Embedded
  private UserEmail email;

  @Embedded
  private PasswordHash passwordHash;

  protected User() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getEmail() {
    return email.value();
  }

  public String getPasswordHash() {
    return passwordHash.value();
  }

  public static final class Builder extends AbstractBuilder<User, Builder> {

    private Builder() {
      super(new User());
    }

    public Builder email(UserEmail email) {
      value.email = email;
      return self();
    }

    public Builder passwordHash(PasswordHash passwordHash) {
      value.passwordHash = passwordHash;
      return self();
    }

    @Override
    protected Builder self() {
      return this;
    }

    @Override
    protected void validate() {
      required(value.email, "email is required");
      required(value.passwordHash, "passwordHash is required");
    }
  }
}

