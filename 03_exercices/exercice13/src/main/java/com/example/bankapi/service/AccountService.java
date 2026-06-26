package com.example.bankapi.service;

import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.model.Account;
import com.example.bankapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Règles métier des comptes bancaires : unicité du numéro, montants strictement
 * positifs, fonds suffisants pour les retraits et virements.
 */
@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account create(String number, String holder) {
        if (repository.findByNumber(number).isPresent()) {
            throw new AccountAlreadyExistsException(number);
        }
        return repository.save(new Account(number, holder, BigDecimal.ZERO));
    }

    public Account getByNumber(String number) {
        return repository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account deposit(String number, BigDecimal amount) {
        requirePositive(amount);
        Account account = getByNumber(number);
        return repository.save(withBalance(account, account.balance().add(amount)));
    }

    public Account withdraw(String number, BigDecimal amount) {
        requirePositive(amount);
        Account account = getByNumber(number);
        requireSufficientFunds(account, amount);
        return repository.save(withBalance(account, account.balance().subtract(amount)));
    }

    public void transfer(String from, String to, BigDecimal amount) {
        requirePositive(amount);
        Account source = getByNumber(from);
        Account destination = getByNumber(to);
        requireSufficientFunds(source, amount);
        repository.save(withBalance(source, source.balance().subtract(amount)));
        repository.save(withBalance(destination, destination.balance().add(amount)));
    }

    private void requirePositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit etre strictement positif");
        }
    }

    private void requireSufficientFunds(Account account, BigDecimal amount) {
        if (account.balance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(account.number());
        }
    }

    private Account withBalance(Account account, BigDecimal newBalance) {
        return new Account(account.number(), account.holder(), newBalance);
    }
}
