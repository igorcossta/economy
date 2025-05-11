package com.github.igorcossta;

import com.github.igorcossta.command.RegisterCommands;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.infra.database.Sqlite;
import com.github.igorcossta.infra.listener.OnJoin;
import com.github.igorcossta.infra.repository.AccountRepositorySqlite;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class Economy extends JavaPlugin {
    public static Economy instance;
    private AccountRepository accountRepository;

    @Override
    public void onEnable() {
        accountRepository = new AccountRepositorySqlite(new Sqlite(getDataFolder()));

        instance = this;
        this.getServer().getPluginManager().registerEvents(new OnJoin(accountRepository), this);
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                commands ->
                        new RegisterCommands(accountRepository, commands.registrar())
        );

    }
}
