package com.github.igorcossta.domain.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface Balance {
    BigDecimal show(UUID uuid);
}
