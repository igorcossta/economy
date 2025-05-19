package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.exception.ReceivingTransactionsDisabledException;
import com.github.igorcossta.domain.exception.SelfTransferNotAllowedException;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Deposit;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositImpl implements Deposit {
    private final AccountRepository accountRepository;

    public DepositImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // todo: get offline player uuid instead and then make the check at service level
    @Override
    public void to(UUID uuid, BigDecimal value) {
        Account account = accountRepository.findByUUID(uuid)
                .orElseThrow(() -> new AccountNotFoundException(uuid));

        if (account.getIdentifier().equals(uuid)) {
            throw new SelfTransferNotAllowedException();
        }

        if (!account.receivesTransactions()) {
            throw new ReceivingTransactionsDisabledException(uuid);
        }

        account.deposit(new Amount(value));
        accountRepository.save(account);
    }
}
