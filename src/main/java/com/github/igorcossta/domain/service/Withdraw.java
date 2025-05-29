package com.github.igorcossta.domain.service;

import com.github.igorcossta.domain.TransactionLog;

import java.math.BigDecimal;
import java.util.UUID;

public interface Withdraw {
    TransactionLog from(UUID executor, UUID target, BigDecimal value);
}
