package com.github.igorcossta.domain.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface Withdraw {
    void from(UUID uuid, BigDecimal value);
}
