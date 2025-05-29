package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.TransactionLog;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.exception.InvalidPlayerException;
import com.github.igorcossta.domain.exception.SelfTransferNotAllowedException;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Withdraw;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class WithDrawImpl implements Withdraw {
    private final AccountRepository accountRepository;

    public WithDrawImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public TransactionLog from(UUID executor, UUID target, BigDecimal value) {
        if (target == null)
            throw new InvalidPlayerException();

        Account executorAcc = accountRepository.findByUUID(executor)
                .orElseThrow(() -> new AccountNotFoundException(executor));
        Account targetAcc = accountRepository.findByUUID(target)
                .orElseThrow(() -> new AccountNotFoundException(target));

        Amount amount = new Amount(value);
        BigDecimal previousReceiverBalance = targetAcc.balance();
        targetAcc.withdraw(amount);
        BigDecimal newReceiverBalance = targetAcc.balance();

        var transactionLog = new TransactionLog(UUID.randomUUID(), "WITHDRAW",
                executorAcc.getOwnerUsername(),
                targetAcc.getOwnerUsername(),
                amount.value(),
                previousReceiverBalance,
                newReceiverBalance,
                null,
                null,
                Instant.now()
        );

        accountRepository.save(targetAcc);
        return transactionLog;
    }
}
