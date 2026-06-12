package com.example;

import java.util.List;

/**
 * Accès au catalogue de produits (isolé via Mockito dans les tests).
 */
public interface ProductRepository {
    List<Product> findAll();
}
