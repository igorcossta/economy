package com.github.igorcossta.command;

import com.github.igorcossta.command.template.Command;
import com.github.igorcossta.domain.service.Balance;
import com.github.igorcossta.utils.Format;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
                .then(Commands.argument("username", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            Bukkit.getOnlinePlayers()
                                    .stream()
                                    .map(Player::getName)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String username = StringArgumentType.getString(context, "username");
                            OfflinePlayer target = Bukkit.getOfflinePlayer(username);

                            Player commandExecutor = (Player) context.getSource().getSender();
                            try {
                                commandExecutor.sendMessage("%s balance is %s".formatted(username,
                                        Format.money(balance.show(target.getUniqueId()))));
                            } catch (Exception e) {
                                commandExecutor.sendMessage(e.getMessage());
                            }
                            return 1;
                        })
                ).executes(context -> {
                    Player player = (Player) context.getSource().getSender();
                    player.sendMessage("Your balance is %s".formatted(Format.money(balance.show(player.getUniqueId()))));
                    return 1;
                });
    }
}
