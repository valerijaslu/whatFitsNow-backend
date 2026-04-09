package com.whatfitsnow.whatfitsnowbackend.tag;

import com.whatfitsnow.whatfitsnowbackend.common.builder.AbstractBuilder;
import com.whatfitsnow.whatfitsnowbackend.common.persistence.AbstractEntity;
import com.whatfitsnow.whatfitsnowbackend.tag.vo.TagName;
import com.whatfitsnow.whatfitsnowbackend.user.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag extends AbstractEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Embedded
  private TagName name;

  protected Tag() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public long getUserId() {
    return user.getId();
  }

  public String getName() {
    return name.value();
  }

  public static final class Builder extends AbstractBuilder<Tag, Builder> {

    private Builder() {
      super(new Tag());
    }

    public Builder user(User user) {
      value.user = user;
      return self();
    }

    public Builder name(TagName name) {
      value.name = name;
      return self();
    }

    @Override
    protected Builder self() {
      return this;
    }

    @Override
    protected void validate() {
      required(value.user, "user is required");
      required(value.name, "name is required");
    }
  }
}

