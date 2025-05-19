package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Balance;

import java.math.BigDecimal;
import java.util.UUID;

public class BalanceImpl implements Balance {
    private final AccountRepository accountRepository;

    public BalanceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public BigDecimal show(UUID uuid) {
        Account account = accountRepository.findByUUID(uuid)
                .orElseThrow(() -> new AccountNotFoundException(uuid));
        return account.balance();
    }
}
