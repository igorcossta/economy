package com.github.igorcossta.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Nested
    class WithdrawBehavior {
        @Test
        @DisplayName("Throws IllegalArgumentException when withdraw amount is null")
        void whenWithdrawAmountIsNullThenThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> underTest.withdraw(null));
            assertEquals("Withdrawal amount cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Subtract withdraw amount to current balance correctly")
        void whenWithdrawAmountIsValidThenBalanceIsDecremented() {
            Amount current = mock(Amount.class);
            Amount withdraw = mock(Amount.class);
            Amount result = new Amount(new BigDecimal(1));

            when(current.subtract(withdraw)).thenReturn(result);

            underTest = new Account(identifier, current, username);
            underTest.withdraw(withdraw);

            assertEquals(result.value(), underTest.balance());
        }
    }

    @Nested
    class AccountConstructorTest {
        Identifier identifier = new Identifier(UUID.randomUUID());
        Amount amount = new Amount(BigDecimal.TEN);
        Username username = new Username("TestUser");
        AccountId accountId = new AccountId(UUID.randomUUID());

        @Nested
        class ConstructorWithoutAccountId {
            @Test
            @DisplayName("Creates Account when all parameters are non-null")
            void whenAllParamsAreNonNullThenAccountIsCreated() {
                assertDoesNotThrow(() -> new Account(identifier, amount, username));
            }

            @Test
            @DisplayName("Throws NPE when identifier is null")
            void whenIdentifierIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(null, amount, username));
            }

            @Test
            @DisplayName("Throws NPE when amount is null")
            void whenAmountIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(identifier, null, username));
            }

            @Test
            @DisplayName("Throws NPE when username is null")
            void whenUsernameIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(identifier, amount, null));
            }
        }

        @Nested
        class ConstructorWithAccountId {
            @Test
            @DisplayName("Creates Account when all parameters are non-null")
            void whenAllParamsAreNonNullThenAccountIsCreated() {
                assertDoesNotThrow(() -> new Account(accountId, identifier, amount, username));
            }

            @Test
            @DisplayName("Throws NPE when accountId is null")
            void whenAccountIdIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(null, identifier, amount, username));
            }

            @Test
            @DisplayName("Throws NPE when identifier is null")
            void whenIdentifierIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(accountId, null, amount, username));
            }

            @Test
            @DisplayName("Throws NPE when amount is null")
            void whenAmountIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(accountId, identifier, null, username));
            }

            @Test
            @DisplayName("Throws NPE when username is null")
            void whenUsernameIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(accountId, identifier, amount, null));
            }
        }

        @Nested
        class FullConstructor {
            Boolean receiveTx = true;
            Boolean receiveNotif = false;
            Boolean showBalance = true;

            @Test
            @DisplayName("Creates Account when all parameters are non-null")
            void whenAllParamsAreNonNullThenAccountIsCreated() {
                assertDoesNotThrow(() -> new Account(accountId, identifier, amount, username, receiveTx, receiveNotif, showBalance));
            }

            @Test
            @DisplayName("Throws NPE when accountId is null")
            void whenAccountIdIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(null, identifier, amount, username, receiveTx, receiveNotif, showBalance));
            }

            @Test
            @DisplayName("Throws NPE when identifier is null")
            void whenIdentifierIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(accountId, null, amount, username, receiveTx, receiveNotif, showBalance));
            }

            @Test
            @DisplayName("Throws NPE when amount is null")
            void whenAmountIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(accountId, identifier, null, username, receiveTx, receiveNotif, showBalance));
            }

            @Test
            @DisplayName("Throws NPE when username is null")
            void whenUsernameIsNullThenThrowNPE() {
                assertThrows(NullPointerException.class, () -> new Account(accountId, identifier, amount, null, receiveTx, receiveNotif, showBalance));
            }
        }
    }
}