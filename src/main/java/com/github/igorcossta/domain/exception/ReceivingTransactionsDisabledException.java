package com.github.igorcossta.domain.exception;

import java.util.UUID;

public class ReceivingTransactionsDisabledException extends RuntimeException {
    public ReceivingTransactionsDisabledException(UUID accountId) {
        super("Account %s can't receive transactions".formatted(accountId));
    }
}
