package com.github.igorcossta.command;

import com.github.igorcossta.command.template.Command;
import com.github.igorcossta.domain.TransactionLog;
import com.github.igorcossta.domain.service.Withdraw;
import com.github.igorcossta.infra.event.DepositSucceededEvent;
import com.github.igorcossta.infra.event.WithdrawSucceededEvent;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

import static io.papermc.paper.command.brigadier.Commands.argument;

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
                .then(argument("username", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            Bukkit.getOnlinePlayers()
                                    .stream()
                                    .map(Player::getName)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .then(argument("amount", DoubleArgumentType.doubleArg())
                                .executes(context -> {
                                    String username = StringArgumentType.getString(context, "username");
                                    double amount = DoubleArgumentType.getDouble(context, "amount");

                                    Player commandExecutor = (Player) context.getSource().getSender();
                                    try {
                                        OfflinePlayer target = Bukkit.getOfflinePlayer(username);
                                        TransactionLog transactionLog = withdraw.from(commandExecutor.getUniqueId(),
                                                target.getUniqueId(),
                                                new BigDecimal(amount));
                                        new WithdrawSucceededEvent(transactionLog).callEvent();
                                    } catch (Exception e) {
                                        commandExecutor.sendMessage(e.getMessage());
                                    }
                                    return 1;
                                }) // when all arguments are passed
                        ).executes(context -> {
                            Player commandExecutor = (Player) context.getSource().getSender();
                            commandExecutor.sendMessage("you need to enter the amount");
                            return 1;
                        }) // if amount argument is not specified
                ).executes(context -> {
                    Player commandExecutor = (Player) context.getSource().getSender();
                    commandExecutor.sendMessage("you need to enter player name");
                    return 1;
                });
    }
}
