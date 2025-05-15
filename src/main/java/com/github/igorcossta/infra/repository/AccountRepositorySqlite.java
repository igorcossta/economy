package com.github.igorcossta.infra.repository;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.infra.database.Sqlite;
import com.github.igorcossta.infra.mapper.Mapper;

import java.util.Optional;
import java.util.UUID;

public class AccountRepositorySqlite implements AccountRepository {
    private final Sqlite database;

    public AccountRepositorySqlite(Sqlite database) {
        this.database = database;
    }

    @Override
    public void save(Account account) {
        database.save(Mapper.toEntity(account));
    }

    @Override
    public Optional<Account> findByUUID(UUID uuid) {
        return database.findByUUID(uuid).map(Mapper::toDomain);
    }
}
