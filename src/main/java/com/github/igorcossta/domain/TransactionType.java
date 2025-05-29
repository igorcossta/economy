package com.github.igorcossta.domain;

public enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER;

    @Override
    public String toString() {
        return name();
    }
}
