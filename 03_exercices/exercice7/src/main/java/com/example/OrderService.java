package com.example;

public class OrderService {

    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Recu passerCommande(String reference, int quantite, ClientType clientType) {
        Product product = productRepository.findByReference(reference);
        if (product == null) {
            throw new CommandeRefuseeException("Produit inconnu");
        }
        if (quantite > product.getStockDisponible()) {
            throw new CommandeRefuseeException("Stock insuffisant");
        }
        double montantTotal = product.getPrixUnitaire() * quantite * (1 - clientType.getRemise());
        return new Recu(product.getReference(), quantite, montantTotal, "Commande confirmée");
    }
}
