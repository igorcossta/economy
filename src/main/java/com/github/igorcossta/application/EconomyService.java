package com.github.igorcossta.application;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EconomyService {
    private Set<Account> accounts = new HashSet<>();

    public void addAcc(UUID uuid) {
        accounts.add(new Account(new Identifier(uuid), new Amount(new BigDecimal(100))));
    }

    public void withdraw(final UUID uuid, final BigDecimal withdraw) {
        Account account = accounts.stream()
                .filter(acc -> acc.getIdentifier().equals(uuid))
                .findFirst()
                .orElseThrow();
        account.withdraw(new Amount(withdraw));
    }

    public void deposit(final UUID uuid, final BigDecimal deposit) {
        Account account = accounts.stream()
                .filter(acc -> acc.getIdentifier().equals(uuid))
                .findFirst()
                .orElseThrow();
        account.deposit(new Amount(deposit));
    }

    public BigDecimal balance(final UUID uuid) {
        Account account = accounts.stream()
                .filter(acc -> acc.getIdentifier().equals(uuid))
                .findFirst()
                .orElseThrow();
        return account.balance();
    }
}
