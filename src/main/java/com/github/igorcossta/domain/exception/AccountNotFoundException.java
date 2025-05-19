package com.github.igorcossta.domain.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(UUID accountId) {
        super("account %s not found".formatted(accountId));
    }
}
