package com.whatfitsnow.whatfitsnowbackend.common.valueobject;

import java.util.Objects;

public abstract class AbstractValueObject<T> {

  protected AbstractValueObject() {
  }

  protected abstract void validate();

  public abstract T value();

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractValueObject<?> that = (AbstractValueObject<?>) o;
    return Objects.equals(value(), that.value());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(value());
  }

  @Override
  public String toString() {
    return String.valueOf(value());
  }
}

