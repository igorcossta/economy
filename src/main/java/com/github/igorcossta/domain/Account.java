package com.github.igorcossta.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {
    private final Identifier identifier;
    private Amount amount;

    public Account(Identifier identifier, Amount amount) {
        this.identifier = Objects.requireNonNull(identifier);
        this.amount = Objects.requireNonNull(amount);
    }

    public void deposit(Amount depositAmount) {
        if (depositAmount == null) {
            throw new IllegalArgumentException("Deposit amount cannot be null");
        }
        this.amount = this.amount.add(depositAmount);
    }

    public void withdraw(Amount withdrawAmount) {
        if (withdrawAmount == null) {
            throw new IllegalArgumentException("Withdrawal amount cannot be null");
        }
        this.amount = this.amount.subtract(withdrawAmount);
    }

    public UUID getIdentifier() {
        return this.identifier.value();
    }

    public BigDecimal balance() {
        return amount.value();
    }
}
