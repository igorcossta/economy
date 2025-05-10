package com.github.igorcossta.command;

import com.github.igorcossta.command.template.Command;
import com.github.igorcossta.domain.service.Deposit;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import static io.papermc.paper.command.brigadier.Commands.argument;

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
                                        Player target = Bukkit.getPlayerExact(username);
                                        Objects.requireNonNull(target, "player must be online");

                                        deposit.to(target.getUniqueId(), new BigDecimal(amount));
                                        commandExecutor.sendMessage("sending to %s amount %s".formatted(username, NumberFormat.getCurrencyInstance(Locale.US).format(amount)));
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
                }); // if no argument is provided
    }
}
