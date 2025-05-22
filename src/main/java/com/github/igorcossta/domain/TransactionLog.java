package com.github.igorcossta.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionLog(
        UUID id,
        String action,
        String sender,
        String receiver,
        BigDecimal amount,
        BigDecimal previousReceiverBalance,
        BigDecimal newReceiverBalance,
        BigDecimal previousSenderBalance,
        BigDecimal newSenderBalance,
        Instant timestamp
) {
}
