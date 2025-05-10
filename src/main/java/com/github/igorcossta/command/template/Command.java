package com.github.igorcossta.command.template;

import com.github.igorcossta.Economy;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.List;

public abstract class Command {
    private final String name;
    private final String description;
    private final String permission;
    private final String[] aliases;

    public Command(String name, String description, String permission, String... aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
    }

    public void register(Commands commands) {
        commands.register(Economy.instance.getPluginMeta(), run(Commands.literal(name)).build(), description, List.of(aliases));
    }

    protected abstract LiteralArgumentBuilder<CommandSourceStack> run(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder);

    protected String getPermission() {
        return permission;
    }
}
