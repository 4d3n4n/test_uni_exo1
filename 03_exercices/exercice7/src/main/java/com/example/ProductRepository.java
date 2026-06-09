package com.example;

public interface ProductRepository {
    Product findByReference(String reference);
}
