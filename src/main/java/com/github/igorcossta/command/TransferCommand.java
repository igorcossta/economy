package com.github.igorcossta.command;

import com.github.igorcossta.command.template.Command;
import com.github.igorcossta.domain.service.Transfer;
import com.github.igorcossta.infra.event.TransferSucceededEvent;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

import static io.papermc.paper.command.brigadier.Commands.argument;

public class TransferCommand extends Command {
    private final Transfer transfer;

    public TransferCommand(String name,
                           String description,
                           String permission, Transfer transfer,
                           String... aliases) {
        super(name, description, permission, aliases);
        this.transfer = transfer;
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
                                        OfflinePlayer receiver = Bukkit.getOfflinePlayer(username);
                                        transfer.execute(commandExecutor.getUniqueId(),
                                                receiver.getUniqueId(),
                                                new BigDecimal(amount));
                                        new TransferSucceededEvent(commandExecutor,
                                                (Player) receiver,
                                                new BigDecimal(amount)).callEvent();
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
