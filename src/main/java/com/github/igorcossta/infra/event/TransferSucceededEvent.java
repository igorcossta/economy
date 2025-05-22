package com.github.igorcossta.infra.event;

import com.github.igorcossta.domain.TransactionLog;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TransferSucceededEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final TransactionLog transactionLog;

    public TransferSucceededEvent(TransactionLog transactionLog) {
        this.transactionLog = transactionLog;
    }

    public TransactionLog getTransactionLog() {
        return transactionLog;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
