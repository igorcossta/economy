package com.github.igorcossta.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountTest {
    Account underTest;
    Identifier identifier;
    Amount amount;
    Username username;

    @BeforeEach
    void beforeEach() {
        identifier = new Identifier(UUID.fromString("22222222-2222-2222-2222-222222222222"));
        amount = new Amount(new BigDecimal(1));
        username = new Username("underTest");

        underTest = new Account(identifier, amount, username);
    }

    @Nested
    class DepositBehavior {
        @Test
        @DisplayName("Throws IllegalArgumentException when deposit amount is null")
        void whenDepositAmountIsNullThenThrowIllegalArgumentException() {
            RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> underTest.deposit(null));
            assertEquals("Deposit amount cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Adds deposit amount to current balance correctly")
        void whenDepositAmountIsValidThenBalanceIsIncremented() {
            Amount current = mock(Amount.class);
            Amount deposit = mock(Amount.class);
            Amount result = new Amount(new BigDecimal(2));

            when(current.add(deposit)).thenReturn(result);

            underTest = new Account(identifier, current, username);
            underTest.deposit(deposit);

            assertEquals(result.value(), underTest.balance());
        }

    }
}