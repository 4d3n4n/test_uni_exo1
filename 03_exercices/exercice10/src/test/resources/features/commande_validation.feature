Feature: Validation de commande

  En tant qu'utilisateur, je veux valider une commande.

  Background:
    Given le produit "P1" "Clavier USB" au prix de 25
    And une commande "C1" vide

  Scenario: Validation d'une commande existante
    Given la commande "C1" contient déjà 1 fois le produit "P1"
    When l'utilisateur valide la commande "C1"
    Then la commande est confirmée

  Scenario: Validation refusée si la commande n'existe pas
    When l'utilisateur valide la commande "INCONNUE"
    Then une erreur de commande inexistante est renvoyée
