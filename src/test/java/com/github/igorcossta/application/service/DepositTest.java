package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;
import com.github.igorcossta.domain.Username;
import com.github.igorcossta.domain.exception.AccountNotFoundException;
import com.github.igorcossta.domain.exception.InvalidPlayerException;
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

class DepositTest {
    UUID receiver;
    UUID sender;

    Account receiverAcc;
    Account senderAcc;

    AccountRepository accountRepository;
    DepositImpl deposit;

    Amount depositAmount;

    @BeforeEach
    void beforeEach() {
        receiver = UUID.fromString("11111111-1111-1111-1111-111111111111");
        sender = UUID.fromString("22222222-2222-2222-2222-222222222222");

        receiverAcc = new Account(new Identifier(receiver), new Amount(TEN), new Username("receiverUnderTest"));
        senderAcc = new Account(new Identifier(sender), new Amount(TEN), new Username("senderUnderTest"));

        accountRepository = mock(AccountRepository.class);
        deposit = new DepositImpl(accountRepository);

        depositAmount = new Amount(TEN);
    }

    @Test
    @DisplayName("When the player tries to deposit money to an nonexisting player, an exception will be launched")
    void whenPlayerNotFoundThenThrowException() {
        when(accountRepository.findByUUID(receiver))
                .thenReturn(Optional.empty());
        when(accountRepository.findByUUID(sender))
                .thenReturn(Optional.of(senderAcc));

        var exception = assertThrowsExactly(AccountNotFoundException.class, () -> deposit.to(sender, receiver, depositAmount.value()));

        assertEquals("account 11111111-1111-1111-1111-111111111111 not found", exception.getMessage());
        verify(accountRepository, times(2)).findByUUID(any(UUID.class));
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

        when(accountRepository.findByUUID(sender))
                .thenReturn(Optional.of(senderAcc));

        var exception = assertThrowsExactly(ReceivingTransactionsDisabledException.class, () -> deposit.to(sender, receiver, depositAmount.value()));

        assertEquals("Account 11111111-1111-1111-1111-111111111111 can't receive transactions", exception.getMessage());
        verify(accountRepository, times(2)).findByUUID(any(UUID.class));
        verify(receiverAcc, never()).deposit(any());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    @DisplayName("When deposit money to player, then balances update correctly")
    void whenDepositMoneyToPlayerThenBalancesAreUpdatedCorrectly() {
        when(accountRepository.findByUUID(receiver))
                .thenReturn(Optional.of(receiverAcc));
        when(accountRepository.findByUUID(sender))
                .thenReturn(Optional.of(senderAcc));

        deposit.to(sender, receiver, depositAmount.value());

        verify(accountRepository, times(2)).findByUUID(any(UUID.class));
        verify(accountRepository, times(1)).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    @DisplayName("When receiver not exists, then exception is thrown")
    void whenReceiverNotExistsThenExceptionIsThrown() {
        InvalidPlayerException exception = assertThrowsExactly(InvalidPlayerException.class,
                () -> deposit.to(sender, null, depositAmount.value()));

        assertEquals("Player cannot be null", exception.getMessage());

        verify(accountRepository, times(0)).findByUUID(any(UUID.class));
        verify(accountRepository, times(0)).save(any(Account.class));
    }

}