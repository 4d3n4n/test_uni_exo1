Feature: Ajout de produit à une commande

  En tant qu'utilisateur, je veux ajouter des produits à ma commande.

  Background:
    Given le produit "P1" "Clavier USB" au prix de 25
    And une commande "C1" vide

  Scenario: Ajout d'un produit à la commande
    When l'utilisateur ajoute le produit "P1" à la commande "C1"
    Then l'ajout est confirmé
    And la commande "C1" contient 1 fois le produit "P1"

  Scenario: Ajout d'un produit déjà présent augmente la quantité
    Given la commande "C1" contient déjà 1 fois le produit "P1"
    When l'utilisateur ajoute le produit "P1" à la commande "C1"
    Then la commande "C1" contient 2 fois le produit "P1"

  Scenario: Ajout refusé si la commande n'existe pas
    When l'utilisateur ajoute le produit "P1" à la commande "INCONNUE"
    Then une erreur de commande inexistante est renvoyée
