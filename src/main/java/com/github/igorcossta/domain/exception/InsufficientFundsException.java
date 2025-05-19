package com.github.igorcossta.domain.exception;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal currentBalance) {
        super("Insufficient funds for withdrawal. Your current balance is " +
                NumberFormat.getCurrencyInstance(Locale.US).format(currentBalance));
    }
}
