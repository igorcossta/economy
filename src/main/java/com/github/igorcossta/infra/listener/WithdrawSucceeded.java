package com.github.igorcossta.infra.listener;

import com.github.igorcossta.domain.TransactionLog;
import com.github.igorcossta.domain.repository.TransactionLogRepository;
import com.github.igorcossta.infra.event.WithdrawSucceededEvent;
import com.github.igorcossta.utils.Format;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WithdrawSucceeded implements Listener {
    private final TransactionLogRepository repository;

    public WithdrawSucceeded(TransactionLogRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    void on(WithdrawSucceededEvent event) {
        TransactionLog transactionLog = event.getTransactionLog();
        Player executor = Bukkit.getPlayer(transactionLog.sender());
        Player target = Bukkit.getPlayer(transactionLog.receiver());

        repository.save(event.getTransactionLog());

        if (executor != null) {
            executor.sendMessage("You withdrew " + Format.money(transactionLog.amount()) + " from " + transactionLog.receiver() + "'s account.");
        }

        if (target != null) {
            target.sendMessage(transactionLog.amount() + " was withdrawn from your account by " + transactionLog.sender() + ".");
        }
    }
}
