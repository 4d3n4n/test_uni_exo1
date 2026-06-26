package com.example.bankapi.repository;

import com.example.bankapi.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stockage en mémoire des comptes, indexés par leur numéro (identifiant fourni).
 */
@Repository
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        accounts.put(account.number(), account);
        return account;
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        return Optional.ofNullable(accounts.get(number));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values())
                .stream()
                .sorted(Comparator.comparing(Account::number))
                .toList();
    }

    @Override
    public void deleteAll() {
        accounts.clear();
    }
}
