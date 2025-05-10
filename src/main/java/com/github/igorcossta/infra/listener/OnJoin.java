package com.github.igorcossta.infra.listener;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;
import com.github.igorcossta.domain.repository.AccountRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class OnJoin implements Listener {
    private final AccountRepository accountRepository;

    public OnJoin(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventHandler
    void on(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Optional<Account> account = accountRepository.findByUUID(uuid);
        if (account.isEmpty()) {
            accountRepository.save(new Account(new Identifier(uuid), new Amount(new BigDecimal(1))));
            event.getPlayer().sendMessage("Welcome! We are creating a new bank account.");
        }
    }
}
