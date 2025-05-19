package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;
import com.github.igorcossta.domain.Username;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.exception.ReceivingTransactionsDisabledException;
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

class DepositImplTest {
    UUID receiver;

    Account receiverAcc;

    AccountRepository accountRepository;
    DepositImpl deposit;

    Amount depositAmount;

    @BeforeEach
    void beforeEach() {
        receiver = UUID.fromString("11111111-1111-1111-1111-111111111111");

        receiverAcc = new Account(new Identifier(receiver), new Amount(TEN), new Username("receiverUnderTest"));

        accountRepository = mock(AccountRepository.class);
        deposit = new DepositImpl(accountRepository);

        depositAmount = new Amount(TEN);
    }

    @Test
    @DisplayName("When the player tries to deposit money to an nonexisting player, an exception will be launched")
    void whenPlayerNotFoundThenThrowException() {
        when(accountRepository.findByUUID(receiver))
                .thenReturn(Optional.empty());

        var exception = assertThrowsExactly(AccountNotFoundException.class, () -> deposit.to(receiver, depositAmount.value()));

        assertEquals("account 11111111-1111-1111-1111-111111111111 not found", exception.getMessage());
        verify(accountRepository).findByUUID(receiver);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    @DisplayName("When the target has the option to not receive transactions, an exception will be launched")
    void whenTargetDisableTransactionThenThrowException() {
        receiverAcc = mock(Account.class);
        when(receiverAcc.getIdentifier())
                .thenReturn(UUID.randomUUID()); // ensure not equal to targetUUID
        when(receiverAcc.receivesTransactions())
                .thenReturn(false);

        when(accountRepository.findByUUID(receiver))
                .thenReturn(Optional.of(receiverAcc));

        var exception = assertThrowsExactly(ReceivingTransactionsDisabledException.class, () -> deposit.to(receiver, depositAmount.value()));

        assertEquals("Account 11111111-1111-1111-1111-111111111111 can't receive transactions", exception.getMessage());
        verify(accountRepository).findByUUID(receiver);
        verify(receiverAcc, never()).deposit(any());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    @DisplayName("When deposit money to player, then balances update correctly")
    void whenDepositMoneyToPlayerThenBalancesAreUpdatedCorrectly() {
        when(accountRepository.findByUUID(receiver))
                .thenReturn(Optional.of(receiverAcc));

        deposit.to(receiver, depositAmount.value());

        verify(accountRepository).findByUUID(receiver);
        verify(accountRepository, times(1)).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }

}