package com.github.igorcossta.command;

import com.github.igorcossta.command.template.Command;
import com.github.igorcossta.domain.service.Balance;
import com.github.igorcossta.utils.Format;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

class BalanceCommand extends Command {
    private final Balance balance;

    BalanceCommand(String name,
                   String description,
                   String permission,
                   Balance balance,
                   String... aliases) {
        super(name, description, permission, aliases);
        this.balance = balance;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> run(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder) {
        return argumentBuilder
                .requires(commandSourceStack -> commandSourceStack.getExecutor() instanceof Player)
                .executes(commandContext -> {
                    Player player = (Player) commandContext.getSource().getSender();
                    player.sendMessage("Your balance is %s".formatted(Format.money(balance.show(player.getUniqueId()))));
                    return 1;
                });
    }
}
