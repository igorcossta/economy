package com.github.igorcossta.infra.repository;

import com.github.igorcossta.domain.TransactionLog;
import com.github.igorcossta.domain.repository.TransactionLogRepository;
import com.github.igorcossta.infra.database.Sqlite;

public class TransactionRepositorySqlite implements TransactionLogRepository {
    private final Sqlite sqlite;

    public TransactionRepositorySqlite(Sqlite database) {
        this.sqlite = database;
    }

    @Override
    public void save(TransactionLog transaction) {
        sqlite.save(transaction);
    }
}
