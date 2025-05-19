package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.exception.ReceivingTransactionsDisabledException;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.exception.InvalidPlayerException;
import com.github.igorcossta.domain.exception.SelfTransferNotAllowedException;
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
            throw new InvalidPlayerException();
        if (sender.equals(receiver))
            throw new SelfTransferNotAllowedException();

        Account senderAcc = accountRepository.findByUUID(sender)
                .orElseThrow(() -> new AccountNotFoundException(sender));
        Account receiverAcc = accountRepository.findByUUID(receiver)
                .orElseThrow(() -> new AccountNotFoundException(receiver));

        if (!receiverAcc.receivesTransactions())
            throw new ReceivingTransactionsDisabledException(receiver);

        Amount amount = new Amount(value);

        senderAcc.withdraw(amount);
        receiverAcc.deposit(amount);

        accountRepository.save(senderAcc);
        accountRepository.save(receiverAcc);
    }
}
