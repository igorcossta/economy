package com.github.igorcossta.domain.exception;

public class SelfTransferNotAllowedException extends RuntimeException {
    public SelfTransferNotAllowedException() {
        super("You cannot send money to yourself");
    }
}
