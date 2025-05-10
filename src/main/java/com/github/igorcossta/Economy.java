package com.github.igorcossta;

import com.github.igorcossta.application.EconomyService;
import com.github.igorcossta.command.RegisterCommands;
import com.github.igorcossta.infra.repository.AccountRepositoryInMemory;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Economy extends JavaPlugin {
    public static Economy instance;
    private EconomyService economyService;

    @Override
    public void onEnable() {
        AccountRepositoryInMemory accountRepositoryInMemory = new AccountRepositoryInMemory();
        economyService = new EconomyService(accountRepositoryInMemory);

        instance = this;
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                commands ->
                        new RegisterCommands(accountRepositoryInMemory, commands.registrar())
        );

        this.getCommand("addacc").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                player.sendMessage("account created");
                economyService.addAcc(player.getUniqueId());
            }
            return true;
        });
    }
}
