package com.github.igorcossta.command;

import com.github.igorcossta.command.template.Command;
import com.github.igorcossta.domain.service.Deposit;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

class DepositCommand extends Command {
    private final Deposit deposit;

    DepositCommand(String name,
                   String description,
                   String permission,
                   Deposit deposit,
                   String... aliases) {
        super(name, description, permission, aliases);
        this.deposit = deposit;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> run(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder) {
        return argumentBuilder
                .requires(commandSourceStack -> commandSourceStack.getExecutor() instanceof Player)
                .executes(commandContext -> {
                    Player player = (Player) commandContext.getSource().getSender();
                    BigDecimal amount = new BigDecimal("100.5");

                    deposit.to(player.getUniqueId(), amount);
                    player.sendMessage("Added to your account $%s".formatted(amount));
                    return 1;
                });
    }
}
