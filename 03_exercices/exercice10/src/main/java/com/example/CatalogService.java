package com.example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Recherche de produits et navigation par catégorie.
 */
public class CatalogService {
    private final ProductRepository productRepository;

    public CatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> searchByKeyword(String keyword) {
        String needle = keyword.toLowerCase();
        return productRepository.findAll().stream()
                .filter(product -> product.getNom().toLowerCase().contains(needle))
                .collect(Collectors.toList());
    }

    public List<Product> searchByMaxPrice(int maxPrice) {
        return productRepository.findAll().stream()
                .filter(product -> product.getPrix() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<Product> findByCategory(String category) {
        return productRepository.findAll().stream()
                .filter(product -> product.getCategorie().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
