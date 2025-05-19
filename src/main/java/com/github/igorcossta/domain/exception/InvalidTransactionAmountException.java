package com.github.igorcossta.domain.exception;

public class InvalidTransactionAmountException extends RuntimeException {
    public InvalidTransactionAmountException() {
        super("Amount must be non-null and non-negative");
    }
}
