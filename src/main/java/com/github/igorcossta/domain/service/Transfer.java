package com.github.igorcossta.domain.service;

import com.github.igorcossta.domain.TransactionLog;

import java.math.BigDecimal;
import java.util.UUID;

public interface Transfer {
    TransactionLog execute(UUID sender, UUID receiver, BigDecimal value);
}
