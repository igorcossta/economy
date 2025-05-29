package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;
import com.github.igorcossta.domain.Username;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Balance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceImplTest {
    UUID target;

    Account account;

    AccountRepository accountRepository;
    Balance balance;

    @BeforeEach
    void beforeEach() {
        target = UUID.fromString("11111111-1111-1111-1111-111111111111");

        account = new Account(new Identifier(target), new Amount(TEN), new Username("senderUnderTest"));

        accountRepository = mock(AccountRepository.class);
        balance = new BalanceImpl(accountRepository);
    }

    @Test
    @DisplayName("When the account exists, then the correct balance should be returned")
    void whenAccountExistsThenReturnBalance() {
        account = mock(Account.class);
        when(accountRepository.findByUUID(target))
                .thenReturn(Optional.of(account));
        when(account.balance())
                .thenReturn(TEN);

        BigDecimal result = balance.show(target);

        assertEquals(TEN, result);
        assertNotNull(result);

        verify(accountRepository, times(1)).findByUUID(any(UUID.class));
        verify(account, times(1)).balance();
        verifyNoMoreInteractions(accountRepository, account);
    }

    @Test
    @DisplayName("When the account does not exist, then an AccountNotFoundException should be thrown")
    void whenAccountDoesNotExistThenThrowAccountNotFoundException() {
        when(accountRepository.findByUUID(target))
                .thenReturn(Optional.empty());

        var exception = assertThrowsExactly(AccountNotFoundException.class, () -> balance.show(target));

        assertEquals("account 11111111-1111-1111-1111-111111111111 not found", exception.getMessage());

        verify(accountRepository, times(1)).findByUUID(any(UUID.class));
        verifyNoMoreInteractions(accountRepository);
    }
}