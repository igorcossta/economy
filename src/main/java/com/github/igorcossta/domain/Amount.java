package com.github.igorcossta.domain;

import java.math.BigDecimal;

public class Amount {
    private final BigDecimal amount;

    public Amount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-null and non-negative");
        }
        this.amount = amount;
    }

    public Amount add(Amount other) {
        return new Amount(this.amount.add(other.amount));
    }

    public Amount subtract(Amount other) {
        if (this.amount.compareTo(other.amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal");
        }
        return new Amount(this.amount.subtract(other.amount));
    }

    public BigDecimal value() {
        return amount;
    }
}
