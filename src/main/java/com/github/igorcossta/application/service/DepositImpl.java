package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.TransactionLog;
import com.github.igorcossta.domain.TransactionType;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.exception.InvalidPlayerException;
import com.github.igorcossta.domain.exception.ReceivingTransactionsDisabledException;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Deposit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

import static java.math.BigInteger.*;

public class DepositImpl implements Deposit {
    private final AccountRepository accountRepository;

    public DepositImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public TransactionLog to(UUID sender, UUID receiver, BigDecimal value) {
        if (receiver == null)
            throw new InvalidPlayerException();

        Account senderAcc = accountRepository.findByUUID(sender)
                .orElseThrow(() -> new AccountNotFoundException(sender));
        Account receiverAcc = accountRepository.findByUUID(receiver)
                .orElseThrow(() -> new AccountNotFoundException(receiver));

        if (!receiverAcc.receivesTransactions()) {
            throw new ReceivingTransactionsDisabledException(receiver);
        }

        Amount amount = new Amount(value);
        BigDecimal previousReceiverBalance = receiverAcc.balance();
        receiverAcc.deposit(amount);
        BigDecimal newReceiverBalance = receiverAcc.balance();

        var transfer = new TransactionLog(UUID.randomUUID(), TransactionType.DEPOSIT,
                senderAcc.getOwnerUsername(),
                receiverAcc.getOwnerUsername(),
                amount.value(),
                previousReceiverBalance,
                newReceiverBalance,
                new BigDecimal(ZERO),
                new BigDecimal(ZERO),
                Instant.now()
        );

        accountRepository.save(receiverAcc);
        return transfer;
    }
}
