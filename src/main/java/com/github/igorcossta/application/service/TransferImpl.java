package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Transfer;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferImpl implements Transfer {
    private final AccountRepository accountRepository;

    public TransferImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // todo: add transaction logger
    @Override
    public void execute(UUID sender, UUID receiver, BigDecimal value) {
        if (receiver == null)
            throw new RuntimeException("Receiver cannot be null");
        if (sender.equals(receiver))
            throw new RuntimeException("You cannot send money to yourself");

        Account senderAcc = accountRepository.findByUUID(sender)
                .orElseThrow(() -> new RuntimeException("account %s not found".formatted(sender)));
        Account receiverAcc = accountRepository.findByUUID(receiver)
                .orElseThrow(() -> new RuntimeException("account %s not found".formatted(receiver)));

        if (!receiverAcc.receivesTransactions())
            throw new RuntimeException("Account %s can't receive transactions".formatted(receiverAcc.getIdentifier()));

        Amount amount = new Amount(value);

        senderAcc.withdraw(amount);
        receiverAcc.deposit(amount);

        accountRepository.save(senderAcc);
        accountRepository.save(receiverAcc);
    }
}
