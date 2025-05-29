package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;
import com.github.igorcossta.domain.Username;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.exception.InvalidPlayerException;
import com.github.igorcossta.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class WithDrawImplTest {
    UUID executor;
    UUID target;

    Account executorAcc;
    Account targetAcc;

    AccountRepository accountRepository;
    WithDrawImpl withDraw;

    Amount withdrawAmount;

    @BeforeEach
    void beforeEach() {
        executor = UUID.fromString("11111111-1111-1111-1111-111111111111");
        target = UUID.fromString("22222222-2222-2222-2222-222222222222");

        executorAcc = new Account(new Identifier(executor), new Amount(TEN), new Username("executorUnderTest"));
        targetAcc = new Account(new Identifier(target), new Amount(TEN), new Username("targetUnderTest"));

        accountRepository = mock(AccountRepository.class);
        withDraw = new WithDrawImpl(accountRepository);

        withdrawAmount = new Amount(TEN);
    }

    @Test
    @DisplayName("When the target player is null, then throw InvalidPlayerException")
    void whenTargetIsNullThenThrowInvalidPlayerException() {
        var exception = assertThrowsExactly(InvalidPlayerException.class, () -> withDraw.from(executor, null, withdrawAmount.value()));

        assertEquals("Player cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("When executor account is not found, then throw AccountNotFoundException")
    void whenExecutorAccountIsNotFoundThenThrowAccountNotFoundException() {
        when(accountRepository.findByUUID(executor))
                .thenReturn(Optional.empty());

        var exception = assertThrowsExactly(AccountNotFoundException.class, () -> withDraw.from(executor, target, withdrawAmount.value()));

        assertEquals("account 11111111-1111-1111-1111-111111111111 not found", exception.getMessage());

        verify(accountRepository, times(1)).findByUUID(any(UUID.class));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    @DisplayName("When target account is not found, then throw AccountNotFoundException")
    void whenTargetAccountIsNotFoundThenThrowAccountNotFoundException() {
        when(accountRepository.findByUUID(executor))
                .thenReturn(Optional.of(executorAcc));
        when(accountRepository.findByUUID(target))
                .thenReturn(Optional.empty());

        var exception = assertThrowsExactly(AccountNotFoundException.class, () -> withDraw.from(executor, target, withdrawAmount.value()));

        assertEquals("account 22222222-2222-2222-2222-222222222222 not found", exception.getMessage());

        verify(accountRepository, times(2)).findByUUID(any(UUID.class));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    @DisplayName("When withdrawal is successful, then account is saved and balance is updated")
    void whenWithdrawalIsSuccessfulThenSaveAccountAndWithdraw() {
        targetAcc = mock(Account.class);
        when(accountRepository.findByUUID(executor))
                .thenReturn(Optional.of(executorAcc));
        when(accountRepository.findByUUID(target))
                .thenReturn(Optional.of(targetAcc));

        withDraw.from(executor, target, withdrawAmount.value());

        verify(accountRepository, times(2)).findByUUID(any(UUID.class));
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(targetAcc, times(1)).withdraw(any(Amount.class));
        verifyNoMoreInteractions(accountRepository);
    }
}