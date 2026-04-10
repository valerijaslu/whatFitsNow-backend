package com.whatfitsnow.whatfitsnowbackend.activity;

import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.ActivityTitle;
import com.whatfitsnow.whatfitsnowbackend.activity.vo.DurationRange;
import com.whatfitsnow.whatfitsnowbackend.common.builder.AbstractBuilder;
import com.whatfitsnow.whatfitsnowbackend.common.persistence.AbstractEntity;
import com.whatfitsnow.whatfitsnowbackend.user.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "activities")
public class Activity extends AbstractEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Embedded
  private ActivityTitle title;

  @Embedded
  private DurationRange duration;

  @Enumerated(EnumType.STRING)
  private EffortLevel effortLevel;

  @Enumerated(EnumType.STRING)
  private LocationType locationType;

  @Enumerated(EnumType.STRING)
  private SocialType socialType;

  private boolean isActive;

  protected Activity() {
  }

  public static Builder builder() {
    return new Builder();
  }

  static Builder builder(Activity existing) {
    return Builder.forExisting(existing);
  }

  public long getUserId() {
    return user.getId();
  }

  public String getTitle() {
    return title.value();
  }

  public int getMinDurationMinutes() {
    return duration.min();
  }

  public int getMaxDurationMinutes() {
    return duration.max();
  }

  public EffortLevel getEffortLevel() {
    return effortLevel;
  }

  public LocationType getLocationType() {
    return locationType;
  }

  public SocialType getSocialType() {
    return socialType;
  }

  public boolean isActive() {
    return isActive;
  }

  public static final class Builder extends AbstractBuilder<Activity, Builder> {

    private Builder() {
      super(new Activity());
      value.isActive = true;
    }

    private Builder(Activity existing) {
      super(existing);
    }

    public static Builder forExisting(Activity existing) {
      return new Builder(existing);
    }

    public Builder user(User user) {
      value.user = user;
      return self();
    }

    public Builder title(ActivityTitle title) {
      value.title = title;
      return self();
    }

    public Builder durationRange(DurationRange duration) {
      value.duration = duration;
      return self();
    }

    public Builder effortLevel(EffortLevel effortLevel) {
      value.effortLevel = effortLevel;
      return self();
    }

    public Builder locationType(LocationType locationType) {
      value.locationType = locationType;
      return self();
    }

    public Builder socialType(SocialType socialType) {
      value.socialType = socialType;
      return self();
    }

    public Builder active(boolean active) {
      value.isActive = active;
      return self();
    }

    @Override
    protected Builder self() {
      return this;
    }

    @Override
    protected void validate() {
      required(value.user, "user is required");
      required(value.title, "title is required");
      required(value.duration, "durationRange is required");
      required(value.effortLevel, "effortLevel is required");
      required(value.locationType, "locationType is required");
      required(value.socialType, "socialType is required");
    }
  }
}

