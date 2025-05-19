package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Withdraw;

import java.math.BigDecimal;
import java.util.UUID;

public class WithDrawImpl implements Withdraw {
    private final AccountRepository accountRepository;

    public WithDrawImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void from(UUID uuid, BigDecimal value) {
        Account account = accountRepository.findByUUID(uuid)
                .orElseThrow(() -> new AccountNotFoundException(uuid));

        account.withdraw(new Amount(value));
        accountRepository.save(account);
    }
}
