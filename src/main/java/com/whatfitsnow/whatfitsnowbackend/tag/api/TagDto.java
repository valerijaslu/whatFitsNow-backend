package com.whatfitsnow.whatfitsnowbackend.tag.api;

import java.time.Instant;

public record TagDto(
    long id,
    long userId,
    String name,
    Instant createdAt,
    Instant updatedAt
) {
}

