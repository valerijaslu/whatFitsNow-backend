package com.whatfitsnow.whatfitsnowbackend.common.builder;

import java.util.Objects;

public abstract class AbstractBuilder<E, B extends AbstractBuilder<E, B>> {

  protected final E value;

  protected AbstractBuilder(E value) {
    this.value = value;
  }

  protected abstract B self();

  protected abstract void validate();

  public final E build() {
    validate();
    return value;
  }

  protected static <V> V required(V value, String message) {
    return Objects.requireNonNull(value, message);
  }
}

