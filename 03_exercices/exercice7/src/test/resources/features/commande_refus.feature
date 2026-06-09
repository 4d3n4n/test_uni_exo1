Feature: Refus de commande

  Une commande est refusée si le produit n'existe pas
  ou si le stock disponible est insuffisant.

  Scenario: Commande refusée si le produit est inconnu
    Given le produit "X999" n'existe pas
    When un client "STANDARD" commande 1 unité du produit "X999"
    Then la commande est refusée avec le message "Produit inconnu"

  Scenario: Commande refusée si le stock est insuffisant
    Given le produit "P100" "Clavier" au prix de 100.0 euros avec un stock de 5
    When un client "STANDARD" commande 10 unités du produit "P100"
    Then la commande est refusée avec le message "Stock insuffisant"
