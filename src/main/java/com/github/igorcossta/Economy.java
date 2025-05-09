package com.github.igorcossta;

import com.github.igorcossta.application.EconomyService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;

public final class Economy extends JavaPlugin {
    private EconomyService economyService;

    @Override
    public void onEnable() {
        economyService = new EconomyService();

        this.getCommand("addacc").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                player.sendMessage("account created");
                economyService.addAcc(player.getUniqueId());
            }
            return true;
        });

        this.getCommand("money").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                player.sendMessage("You have %s".formatted(economyService.balance(player.getUniqueId())));
            }
            return true;
        });
        this.getCommand("withdraw").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                economyService.withdraw(player.getUniqueId(), new BigDecimal(10));
            }
            return true;
        });

        this.getCommand("deposit").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                economyService.deposit(player.getUniqueId(), new BigDecimal("5.1"));
            }
            return true;
        });
    }
}
