package com.github.igorcossta.application.service;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;
import com.github.igorcossta.domain.Username;
import com.github.igorcossta.domain.repository.AccountRepository;
import com.github.igorcossta.domain.service.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransferTest {
    UUID sender;
    UUID receiver;

    Account senderAcc;
    Account receiverAcc;

    AccountRepository accountRepository;
    Transfer transfer;

    Amount transferAmount;

    @BeforeEach
    void beforeEach() {
        sender = UUID.fromString("11111111-1111-1111-1111-111111111111");
        receiver = UUID.fromString("22222222-2222-2222-2222-222222222222");

        senderAcc = new Account(new Identifier(sender), new Amount(TEN), new Username("senderUnderTest"));
        receiverAcc = new Account(new Identifier(receiver), new Amount(ZERO), new Username("receiverUnderTest"));

        accountRepository = mock(AccountRepository.class);
        transfer = new TransferImpl(accountRepository);

        transferAmount = new Amount(TEN);
    }

    @Test
    @DisplayName("When sender transfers money to another player, then balances update correctly")
    void whenSenderTransfersMoneyThenBalancesAreUpdatedCorrectly() {
        when(accountRepository.findByUUID(sender)).thenReturn(Optional.of(senderAcc));
        when(accountRepository.findByUUID(receiver)).thenReturn(Optional.of(receiverAcc));
        doNothing().when(accountRepository).save(any(Account.class));

        transfer.execute(sender, receiver, transferAmount.value());

        assertEquals(ZERO, senderAcc.balance());
        assertEquals(TEN, receiverAcc.balance());

        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    @DisplayName("When sender transfers money to self, then exception is thrown")
    void whenSenderTransfersMoneyToSelfThenExceptionIsThrown() {
        when(accountRepository.findByUUID(sender)).thenReturn(Optional.of(senderAcc));
        when(accountRepository.findByUUID(receiver)).thenReturn(Optional.of(senderAcc));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transfer.execute(sender, sender, transferAmount.value()));

        assertEquals("You cannot send money to yourself", exception.getMessage());

        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    @DisplayName("When receiver has transfers disabled, then exception is thrown")
    void whenReceiverHasTransfersDisabledThenExceptionIsThrown() {
        receiverAcc = mock(Account.class);

        when(accountRepository.findByUUID(sender)).thenReturn(Optional.of(senderAcc));
        when(accountRepository.findByUUID(receiver)).thenReturn(Optional.of(receiverAcc));
        when(receiverAcc.receivesTransactions()).thenReturn(false);
        when(receiverAcc.getIdentifier()).thenReturn(receiver);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transfer.execute(sender, receiver, transferAmount.value()));

        assertEquals("Account 22222222-2222-2222-2222-222222222222 can't receive transactions", exception.getMessage());

        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    @DisplayName("When receiver not exists, then exception is thrown")
    void whenReceiverNotExistsThenExceptionIsThrown() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transfer.execute(sender, null, transferAmount.value()));

        assertEquals("Receiver cannot be null", exception.getMessage());

        verify(accountRepository, times(0)).findByUUID(any(UUID.class));
        verify(accountRepository, times(0)).save(any(Account.class));
    }
}