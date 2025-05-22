package com.github.igorcossta.infra.listener;

import com.github.igorcossta.domain.TransactionLog;
import com.github.igorcossta.domain.repository.TransactionLogRepository;
import com.github.igorcossta.infra.event.TransferSucceededEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.NumberFormat;
import java.util.Locale;

public class TransferSucceeded implements Listener {
    private final TransactionLogRepository repository;

    public TransferSucceeded(TransactionLogRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    void on(TransferSucceededEvent event) {
        TransactionLog transactionLog = event.getTransactionLog();
        Player sender = Bukkit.getPlayer(transactionLog.sender());
        Player receiver = Bukkit.getPlayer(transactionLog.receiver());

        repository.save(event.getTransactionLog());

        if (sender != null) {
            sender.sendMessage("You sent " +
                    NumberFormat.getCurrencyInstance(Locale.US).format(transactionLog.amount()) + " to " +
                    transactionLog.receiver());
        }

        if (receiver != null) {
            receiver.sendMessage("You received " +
                    NumberFormat.getCurrencyInstance(Locale.US).format(transactionLog.amount()) + " from " +
                    transactionLog.sender());
        }
    }
}
