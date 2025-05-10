package com.github.igorcossta.domain.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface Deposit {
    void to(UUID uuid, BigDecimal value);
}
