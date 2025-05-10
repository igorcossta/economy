package com.github.igorcossta.domain.repository;

import com.github.igorcossta.domain.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    void save(Account account);

    Optional<Account> findByUUID(UUID uuid);
}
