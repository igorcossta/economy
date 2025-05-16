package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;
import com.github.igorcossta.domain.Username;
import com.github.igorcossta.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DepositImplTest {
    private AccountRepository accountRepository;
    private DepositImpl deposit;

    @BeforeEach
    void beforeEach() {
        accountRepository = mock(AccountRepository.class);
        deposit = new DepositImpl(accountRepository);
    }

    @Test
    @DisplayName("When the player tries to deposit money to an nonexisting player, an exception will be launched")
    void whenPlayerNotFoundThenThrowException() {
        UUID uuid = UUID.fromString("efb6ab8a-90b4-4ab1-b6d0-76aaf8f79b10");
        BigDecimal amount = new BigDecimal(100);

        when(accountRepository.findByUUID(uuid))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> deposit.to(uuid, amount));

        assertEquals("account efb6ab8a-90b4-4ab1-b6d0-76aaf8f79b10 not found", exception.getMessage());
        verify(accountRepository).findByUUID(uuid);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    @DisplayName("When the player tries to deposit money to himself, an exception will be launched")
    void whenPlayerUuidIsEqualThenThrowException() {
        UUID uuid = UUID.fromString("efb6ab8a-90b4-4ab1-b6d0-76aaf8f79b10");
        BigDecimal amount = new BigDecimal(100);
        Account account = new Account(new Identifier(uuid), new Amount(new BigDecimal(0)), new Username("underTest"));

        when(accountRepository.findByUUID(uuid))
                .thenReturn(Optional.of(account));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> deposit.to(uuid, amount));

        assertEquals("You cannot send money to yourself", exception.getMessage());
        assertEquals(uuid, account.getIdentifier());
        verify(accountRepository).findByUUID(uuid);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    @DisplayName("When the target has the option to not receive transactions, an exception will be launched")
    void whenTargetDisableTransactionThenThrowException() {
        UUID targetUUID = UUID.fromString("22222222-2222-2222-2222-222222222222");

        BigDecimal amount = new BigDecimal(100);

        Account targetAccount = mock(Account.class);
        when(targetAccount.getIdentifier())
                .thenReturn(UUID.randomUUID()); // ensure not equal to targetUUID
        when(targetAccount.receivesTransactions())
                .thenReturn(false);

        when(accountRepository.findByUUID(targetUUID))
                .thenReturn(Optional.of(targetAccount));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> deposit.to(targetUUID, amount));

        assertEquals("Account 22222222-2222-2222-2222-222222222222 can't receive transactions", exception.getMessage());
        verify(accountRepository).findByUUID(targetUUID);
        verify(targetAccount, never()).deposit(any());
        verifyNoMoreInteractions(accountRepository);
    }

}