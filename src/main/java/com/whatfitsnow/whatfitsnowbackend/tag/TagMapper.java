package com.whatfitsnow.whatfitsnowbackend.tag;

import com.whatfitsnow.whatfitsnowbackend.tag.api.TagDto;

public final class TagMapper {
  private TagMapper() {
  }

  public static TagDto toDto(Tag t) {
    return new TagDto(
        t.getId(),
        t.getUserId(),
        t.getName(),
        t.getCreatedAt(),
        t.getUpdatedAt()
    );
  }
}

