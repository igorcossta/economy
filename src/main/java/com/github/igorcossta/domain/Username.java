package com.github.igorcossta.domain;

import java.util.Objects;

public record Username(String value) {
    public Username {
        Objects.requireNonNull(value, "Username cannot be null");
    }
}
