package com.github.igorcossta.domain;

import java.util.Objects;
import java.util.UUID;

public record Identifier(UUID value) {
    public Identifier {
        Objects.requireNonNull(value, "Identifier cannot be null");
    }
}
