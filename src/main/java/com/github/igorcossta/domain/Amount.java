package com.github.igorcossta.domain;

import com.github.igorcossta.domain.exception.BalanceLimitExceedException;
import com.github.igorcossta.domain.exception.InsufficientFundsException;
import com.github.igorcossta.domain.exception.InvalidTransactionAmountException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Amount {
    private final BigDecimal amount;
    private final BigDecimal MAX_BALANCE = new BigDecimal(999_999_999);
    private final int SCALE = 2;
    private final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    public Amount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionAmountException();
        }
        this.amount = amount;
    }

    public Amount add(Amount other) {
        BigDecimal addedValue = this.amount.add(other.amount);
        BigDecimal scaledAmount = addedValue.setScale(SCALE, ROUNDING);
        if (scaledAmount.compareTo(MAX_BALANCE) > 0) {
            throw new BalanceLimitExceedException(MAX_BALANCE);
        }
        return new Amount(scaledAmount);
    }

    public Amount subtract(Amount other) {
        if (this.amount.compareTo(other.amount) < 0) {
            throw new InsufficientFundsException(amount);
        }
        return new Amount(this.amount.subtract(other.amount));
    }

    public BigDecimal value() {
        return amount;
    }
}
