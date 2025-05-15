package com.github.igorcossta.infra.database;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountEntity {
    private UUID accountId;
    private UUID identifier;
    private BigDecimal amount;
    private String username;

    // settings
    private boolean receiveTransactions;
    private boolean receiveNotifications;
    private boolean showBalanceOnJoin;

    public AccountEntity(UUID accountId,
                         UUID identifier,
                         BigDecimal amount,
                         String username,
                         boolean receiveTransactions,
                         boolean receiveNotifications,
                         boolean showBalanceOnJoin) {
        this.accountId = accountId == null ? UUID.randomUUID() : accountId;
        this.identifier = identifier;
        this.amount = amount;
        this.username = username;
        this.receiveTransactions = receiveTransactions;
        this.receiveNotifications = receiveNotifications;
        this.showBalanceOnJoin = showBalanceOnJoin;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUsername() {
        return username;
    }

    public boolean isReceiveTransactions() {
        return receiveTransactions;
    }

    public boolean isReceiveNotifications() {
        return receiveNotifications;
    }

    public boolean isShowBalanceOnJoin() {
        return showBalanceOnJoin;
    }
}
