package com.github.igorcossta.infra.listener;

import com.github.igorcossta.domain.TransactionLog;
import com.github.igorcossta.domain.repository.TransactionLogRepository;
import com.github.igorcossta.infra.event.DepositSucceededEvent;
import com.github.igorcossta.utils.Format;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DepositSucceeded implements Listener {
    private final TransactionLogRepository repository;

    public DepositSucceeded(TransactionLogRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    void on(DepositSucceededEvent event) {
        TransactionLog transactionLog = event.getTransactionLog();
        Player sender = Bukkit.getPlayer(transactionLog.sender());
        Player receiver = Bukkit.getPlayer(transactionLog.receiver());

        repository.save(event.getTransactionLog());

        if (sender != null) {
            sender.sendMessage("You sent " + Format.money(transactionLog.amount()) + " to " + transactionLog.receiver());
        }

        if (receiver != null) {
            receiver.sendMessage("You received " + Format.money(transactionLog.amount()) + " from " + transactionLog.sender());
        }
    }
}
