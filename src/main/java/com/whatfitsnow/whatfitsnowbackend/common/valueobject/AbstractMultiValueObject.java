package com.whatfitsnow.whatfitsnowbackend.common.valueobject;

import java.util.List;
import java.util.Objects;

public abstract class AbstractMultiValueObject {

  protected AbstractMultiValueObject() {
  }

  protected abstract void validate();

  protected abstract List<Object> equalityComponents();

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractMultiValueObject that = (AbstractMultiValueObject) o;
    return Objects.equals(equalityComponents(), that.equalityComponents());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(equalityComponents());
  }
}

