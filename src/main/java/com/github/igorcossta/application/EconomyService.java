package com.github.igorcossta.application;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;
import com.github.igorcossta.domain.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class EconomyService {
    private final AccountRepository accountRepository;

    public EconomyService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void addAcc(UUID uuid) {
        accountRepository.save(new Account(new Identifier(uuid), new Amount(new BigDecimal("100.55"))));
    }

    public void withdraw(final UUID uuid, final BigDecimal withdraw) {
        Account account = accountRepository.findByUUID(uuid)
                .orElseThrow(() -> new RuntimeException("account %s not found".formatted(uuid)));

        account.withdraw(new Amount(withdraw));
        accountRepository.save(account);
    }

    public void deposit(final UUID uuid, final BigDecimal deposit) {
        Account account = accountRepository.findByUUID(uuid)
                .orElseThrow(() -> new RuntimeException("account %s not found".formatted(uuid)));

        account.deposit(new Amount(deposit));
        accountRepository.save(account);
    }

    public BigDecimal balance(final UUID uuid) {
        Account account = accountRepository.findByUUID(uuid)
                .orElseThrow(() -> new RuntimeException("account %s not found".formatted(uuid)));
        return account.balance();
    }
}
