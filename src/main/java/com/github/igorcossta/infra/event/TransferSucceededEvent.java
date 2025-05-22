package com.github.igorcossta.infra.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class TransferSucceededEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player sender;
    private final Player receiver;
    private final BigDecimal amount;

    public TransferSucceededEvent(Player sender, Player receiver, BigDecimal amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
