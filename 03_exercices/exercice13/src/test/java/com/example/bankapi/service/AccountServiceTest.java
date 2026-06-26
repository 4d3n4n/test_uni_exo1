package com.example.bankapi.service;

import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.model.Account;
import com.example.bankapi.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    private static BigDecimal euros(int value) {
        return BigDecimal.valueOf(value);
    }

    // ---------- Création ----------

    @Test
    void shouldCreateAccount_withZeroBalance() {
        // Arrange
        when(repository.findByNumber("C1")).thenReturn(Optional.empty());
        when(repository.save(any(Account.class)))
                .thenReturn(new Account("C1", "Alice", BigDecimal.ZERO));

        // Act
        Account result = service.create("C1", "Alice");

        // Assert
        assertEquals("C1", result.number());
        assertEquals("Alice", result.holder());
        assertEquals(0, result.balance().compareTo(BigDecimal.ZERO));
        verify(repository).save(argThat(account ->
                account.number().equals("C1")
                        && account.holder().equals("Alice")
                        && account.balance().compareTo(BigDecimal.ZERO) == 0));
    }

    @Test
    void shouldRejectCreation_whenNumberAlreadyExists() {
        // Arrange
        when(repository.findByNumber("C1"))
                .thenReturn(Optional.of(new Account("C1", "Alice", BigDecimal.ZERO)));

        // Act + Assert
        assertThrows(AccountAlreadyExistsException.class, () -> service.create("C1", "Bob"));
        verify(repository, never()).save(any(Account.class));
    }

    // ---------- Consultation ----------

    @Test
    void shouldReturnAccount_whenItExists() {
        // Arrange
        when(repository.findByNumber("C1"))
                .thenReturn(Optional.of(new Account("C1", "Alice", euros(100))));

        // Act
        Account result = service.getByNumber("C1");

        // Assert
        assertEquals("C1", result.number());
        verify(repository).findByNumber("C1");
    }

    @Test
    void shouldThrowNotFound_whenAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("X")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class, () -> service.getByNumber("X"));
    }

    @Test
    void shouldReturnAllAccounts() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(
                new Account("C1", "Alice", euros(100)),
                new Account("C2", "Bob", euros(50))));

        // Act
        List<Account> result = service.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    // ---------- Dépôt ----------

    @Test
    void shouldDeposit_whenAmountIsPositive() {
        // Arrange
        when(repository.findByNumber("C1"))
                .thenReturn(Optional.of(new Account("C1", "Alice", euros(100))));
        when(repository.save(any(Account.class)))
                .thenReturn(new Account("C1", "Alice", euros(150)));

        // Act
        Account result = service.deposit("C1", euros(50));

        // Assert
        assertEquals(0, result.balance().compareTo(euros(150)));
        verify(repository).save(argThat(account -> account.balance().compareTo(euros(150)) == 0));
    }

    @Test
    void shouldRejectDeposit_whenAmountIsZero() {
        assertThrows(IllegalArgumentException.class, () -> service.deposit("C1", BigDecimal.ZERO));
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void shouldRejectDeposit_whenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> service.deposit("C1", euros(-10)));
        verify(repository, never()).save(any(Account.class));
    }

    // ---------- Retrait ----------

    @Test
    void shouldWithdraw_whenAmountIsPositiveAndFundsAreSufficient() {
        // Arrange
        when(repository.findByNumber("C1"))
                .thenReturn(Optional.of(new Account("C1", "Alice", euros(100))));
        when(repository.save(any(Account.class)))
                .thenReturn(new Account("C1", "Alice", euros(60)));

        // Act
        Account result = service.withdraw("C1", euros(40));

        // Assert
        assertEquals(0, result.balance().compareTo(euros(60)));
        verify(repository).save(argThat(account -> account.balance().compareTo(euros(60)) == 0));
    }

    @Test
    void shouldRejectWithdraw_whenAmountIsZero() {
        assertThrows(IllegalArgumentException.class, () -> service.withdraw("C1", BigDecimal.ZERO));
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void shouldRejectWithdraw_whenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> service.withdraw("C1", euros(-10)));
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void shouldRejectWithdraw_whenFundsAreInsufficient() {
        // Arrange
        when(repository.findByNumber("C1"))
                .thenReturn(Optional.of(new Account("C1", "Alice", euros(30))));

        // Act + Assert
        assertThrows(InsufficientFundsException.class, () -> service.withdraw("C1", euros(100)));
        verify(repository, never()).save(any(Account.class));
    }

    // ---------- Virement ----------

    @Test
    void shouldTransfer_whenAmountIsPositiveAndFundsAreSufficient() {
        // Arrange
        when(repository.findByNumber("C1"))
                .thenReturn(Optional.of(new Account("C1", "Alice", euros(100))));
        when(repository.findByNumber("C2"))
                .thenReturn(Optional.of(new Account("C2", "Bob", euros(50))));

        // Act
        service.transfer("C1", "C2", euros(30));

        // Assert : débit de l'émetteur et crédit du destinataire
        verify(repository).save(argThat(account ->
                account.number().equals("C1") && account.balance().compareTo(euros(70)) == 0));
        verify(repository).save(argThat(account ->
                account.number().equals("C2") && account.balance().compareTo(euros(80)) == 0));
    }

    @Test
    void shouldRejectTransfer_whenAmountIsZero() {
        assertThrows(IllegalArgumentException.class, () -> service.transfer("C1", "C2", BigDecimal.ZERO));
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void shouldRejectTransfer_whenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> service.transfer("C1", "C2", euros(-10)));
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void shouldRejectTransfer_whenFundsAreInsufficient() {
        // Arrange
        when(repository.findByNumber("C1"))
                .thenReturn(Optional.of(new Account("C1", "Alice", euros(20))));
        when(repository.findByNumber("C2"))
                .thenReturn(Optional.of(new Account("C2", "Bob", euros(0))));

        // Act + Assert
        assertThrows(InsufficientFundsException.class, () -> service.transfer("C1", "C2", euros(100)));
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void shouldRejectTransfer_whenSourceAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("X")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class, () -> service.transfer("X", "C2", euros(50)));
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void shouldRejectTransfer_whenDestinationAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("C1"))
                .thenReturn(Optional.of(new Account("C1", "Alice", euros(100))));
        when(repository.findByNumber("X")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class, () -> service.transfer("C1", "X", euros(50)));
        verify(repository, never()).save(any(Account.class));
    }
}
