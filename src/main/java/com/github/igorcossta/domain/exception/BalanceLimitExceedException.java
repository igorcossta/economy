package com.github.igorcossta.domain.exception;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class BalanceLimitExceedException extends RuntimeException {
    public BalanceLimitExceedException(BigDecimal amount) {
        super("Account has reached the currency limit of " +
                NumberFormat.getCurrencyInstance(Locale.US).format(amount));
    }
}
