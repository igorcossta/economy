package com.github.igorcossta.command;

import com.github.igorcossta.application.service.BalanceImpl;
import com.github.igorcossta.application.service.DepositImpl;
import com.github.igorcossta.application.service.TransferImpl;
import com.github.igorcossta.application.service.WithDrawImpl;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Balance;
import com.github.igorcossta.domain.service.Deposit;
import com.github.igorcossta.domain.service.Transfer;
import com.github.igorcossta.domain.service.Withdraw;
import io.papermc.paper.command.brigadier.Commands;

public class RegisterCommands {
    private final Balance balance;
    private final Withdraw withdraw;
    private final Deposit deposit;
    private final Transfer transfer;

    public RegisterCommands(AccountRepository accountRepository, Commands commands) {
        this.balance = new BalanceImpl(accountRepository);
        this.withdraw = new WithDrawImpl(accountRepository);
        this.deposit = new DepositImpl(accountRepository);
        this.transfer = new TransferImpl(accountRepository);

        new BalanceCommand("balance",
                "show your current balance",
                "economy.balance",
                balance,
                "money")
                .register(commands);
        new WithdrawCommand("withdraw",
                "subtract your money",
                "economy.withdraw",
                withdraw)
                .register(commands);
        new DepositCommand("deposit",
                "add to your current balance",
                "economy.deposit",
                deposit)
                .register(commands);
        new TransferCommand("send",
                "transfer money to other player",
                "economy.transfer",
                transfer)
                .register(commands);
    }
}
