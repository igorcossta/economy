package com.github.igorcossta.infra.repository;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.repository.AccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AccountRepositoryInMemory implements AccountRepository {
    private Map<UUID, Account> accounts = new HashMap<>();

    @Override
    public void save(Account account) {
        accounts.put(account.getIdentifier(), account);
    }

    @Override
    public Optional<Account> findByUUID(UUID uuid) {
        return Optional.ofNullable(accounts.get(uuid));
    }
}
