package com.github.igorcossta.domain.exception;

public class InvalidPlayerException extends NullPointerException {
    public InvalidPlayerException() {
        super("Player cannot be null");
    }
}
