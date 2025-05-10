package com.github.igorcossta.command;

import com.github.igorcossta.command.template.Command;
import com.github.igorcossta.domain.service.Withdraw;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

class WithdrawCommand extends Command {
    private final Withdraw withdraw;

    WithdrawCommand(String name,
                    String description,
                    String permission,
                    Withdraw withdraw,
                    String... aliases) {
        super(name, description, permission, aliases);
        this.withdraw = withdraw;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> run(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder) {
        return argumentBuilder
                .requires(commandSourceStack -> commandSourceStack.getExecutor() instanceof Player)
                .executes(commandContext -> {
                    Player player = (Player) commandContext.getSource().getSender();
                    BigDecimal amount = new BigDecimal(10);

                    withdraw.from(player.getUniqueId(), amount);
                    player.sendMessage("Subtracted from your account $%s".formatted(amount));
                    return 1;
                });
    }
}
