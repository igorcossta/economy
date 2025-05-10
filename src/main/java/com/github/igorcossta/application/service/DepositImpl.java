package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Deposit;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositImpl implements Deposit {
    private final AccountRepository accountRepository;

    public DepositImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void to(UUID uuid, BigDecimal value) {
        Account account = accountRepository.findByUUID(uuid)
                .orElseThrow(() -> new RuntimeException("account %s not found".formatted(uuid)));

        account.deposit(new Amount(value));
        accountRepository.save(account);
    }
}
