package com.github.igorcossta.infra.listener;

import com.github.igorcossta.infra.event.TransferSucceededEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.NumberFormat;
import java.util.Locale;

public class TransferSucceeded implements Listener {
    @EventHandler
    void on(TransferSucceededEvent event) {
        event.getSender().sendMessage("You sent " +
                NumberFormat.getCurrencyInstance(Locale.US).format(event.getAmount()) + " to " +
                event.getReceiver().getName());

        event.getReceiver().sendMessage("You received " +
                NumberFormat.getCurrencyInstance(Locale.US).format(event.getAmount()) + " from " +
                event.getSender().getName());
    }
}
