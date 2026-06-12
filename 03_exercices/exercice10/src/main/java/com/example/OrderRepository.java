package com.example;

/**
 * Accès aux commandes (isolé via Mockito dans les tests).
 */
public interface OrderRepository {
    Order findById(String orderId);

    void save(Order order);
}
