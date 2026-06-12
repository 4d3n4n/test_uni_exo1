package com.example;

/**
 * Accès aux comptes utilisateurs (isolé via Mockito dans les tests).
 */
public interface UserRepository {
    User findByUsername(String username);

    void save(User user);
}
