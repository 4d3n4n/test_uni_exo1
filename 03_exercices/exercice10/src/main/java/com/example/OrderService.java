package com.example;

/**
 * Gère l'ajout, la suppression de produits et la validation d'une commande.
 */
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Confirmation addProduct(String orderId, String productId) {
        Order order = requireOrder(orderId);
        order.addProduct(productId);
        orderRepository.save(order);
        return new Confirmation("Produit " + productId + " ajouté à la commande " + orderId);
    }

    public void removeProduct(String orderId, String productId) {
        Order order = requireOrder(orderId);
        if (!order.contains(productId)) {
            throw new ProduitAbsentException(productId);
        }
        order.removeProduct(productId);
        orderRepository.save(order);
    }

    public Confirmation validate(String orderId) {
        Order order = requireOrder(orderId);
        return new Confirmation("Commande " + order.getId() + " validée");
    }

    private Order requireOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new CommandeInexistanteException(orderId);
        }
        return order;
    }
}
