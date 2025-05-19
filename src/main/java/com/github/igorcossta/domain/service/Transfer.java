package com.github.igorcossta.domain.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface Transfer {
    void execute(UUID sender, UUID receiver, BigDecimal value);
}
