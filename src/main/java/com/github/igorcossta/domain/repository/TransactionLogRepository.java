package com.github.igorcossta.domain.repository;

import com.github.igorcossta.domain.TransactionLog;

public interface TransactionLogRepository {
    void save(TransactionLog transaction);
}
