Feature: Suppression de produit d'une commande

  En tant qu'utilisateur, je veux supprimer des produits de ma commande.

  Background:
    Given le produit "P1" "Clavier USB" au prix de 25
    And une commande "C1" vide

  Scenario: Diminuer la quantité d'un produit présent en plusieurs exemplaires
    Given la commande "C1" contient déjà 2 fois le produit "P1"
    When l'utilisateur supprime le produit "P1" de la commande "C1"
    Then la commande "C1" contient 1 fois le produit "P1"

  Scenario: Retirer un produit présent en un seul exemplaire
    Given la commande "C1" contient déjà 1 fois le produit "P1"
    When l'utilisateur supprime le produit "P1" de la commande "C1"
    Then la commande "C1" ne contient pas le produit "P1"

  Scenario: Suppression refusée si le produit n'est pas dans la commande
    When l'utilisateur supprime le produit "P1" de la commande "C1"
    Then une erreur de produit absent est renvoyée

  Scenario: Suppression refusée si la commande n'existe pas
    When l'utilisateur supprime le produit "P1" de la commande "INCONNUE"
    Then une erreur de commande inexistante est renvoyée
