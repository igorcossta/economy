package com.github.igorcossta.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {
    private AccountId accountId;
    private final Identifier identifier;
    private Amount amount;
    private Username username;
    private AccountSettings accountSettings;

    public Account(Identifier identifier, Amount amount, Username username) {
        this.identifier = Objects.requireNonNull(identifier);
        this.amount = Objects.requireNonNull(amount);
        this.username = Objects.requireNonNull(username);
        this.accountSettings = new AccountSettings();
    }

    public Account(AccountId accountId, Identifier identifier, Amount amount, Username username) {
        this.accountId = Objects.requireNonNull(accountId);
        this.identifier = Objects.requireNonNull(identifier);
        this.amount = Objects.requireNonNull(amount);
        this.username = Objects.requireNonNull(username);
        this.accountSettings = new AccountSettings();
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

    public String getOwnerUsername() {
        return this.username.value();
    }

    public boolean receivesTransactions() {
        return this.accountSettings.isReceiveTransactions();
    }

    public boolean receivesNotifications() {
        return this.accountSettings.isReceiveNotifications();
    }

    public boolean showsBalanceOnJoin() {
        return this.accountSettings.isShowBalanceOnJoin();
    }

    public UUID getAccountId() {
        return this.accountId.value();
    }

    public UUID getIdentifier() {
        return this.identifier.value();
    }

    public BigDecimal balance() {
        return amount.value();
    }
}
