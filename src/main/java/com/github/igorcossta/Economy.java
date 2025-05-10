package com.github.igorcossta;

import com.github.igorcossta.application.EconomyService;
import com.github.igorcossta.application.service.BalanceImpl;
import com.github.igorcossta.application.service.DepositImpl;
import com.github.igorcossta.application.service.WithDrawImpl;
import com.github.igorcossta.domain.service.Balance;
import com.github.igorcossta.domain.service.Deposit;
import com.github.igorcossta.domain.service.Withdraw;
import com.github.igorcossta.infra.repository.AccountRepositoryInMemory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;

public final class Economy extends JavaPlugin {
    private EconomyService economyService;
    private Balance balance;
    private Deposit deposit;
    private Withdraw withdraw;

    @Override
    public void onEnable() {
        AccountRepositoryInMemory accountRepositoryInMemory = new AccountRepositoryInMemory();
        economyService = new EconomyService(accountRepositoryInMemory);
        balance = new BalanceImpl(accountRepositoryInMemory);
        deposit = new DepositImpl(accountRepositoryInMemory);
        withdraw = new WithDrawImpl(accountRepositoryInMemory);

        this.getCommand("addacc").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                player.sendMessage("account created");
                economyService.addAcc(player.getUniqueId());
            }
            return true;
        });

        this.getCommand("money").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                player.sendMessage("You have %s".formatted(balance.show(player.getUniqueId())));
            }
            return true;
        });
        this.getCommand("withdraw").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                withdraw.from(player.getUniqueId(), new BigDecimal(10));
            }
            return true;
        });

        this.getCommand("deposit").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                deposit.to(player.getUniqueId(), new BigDecimal("5.1"));
            }
            return true;
        });
    }
}
