Feature: Application de la remise selon le profil client

  Le montant total d'une commande dépend du profil du client :
  STANDARD 0 %, PREMIUM 10 %, VIP 20 %.

  Background:
    Given le produit "P100" "Clavier" au prix de 100.0 euros avec un stock de 10

  Scenario: Commande acceptée pour un client STANDARD
    When un client "STANDARD" commande 2 unités du produit "P100"
    Then la commande est acceptée
    And le montant total est de 200.0 euros

  Scenario: Commande acceptée pour un client PREMIUM
    When un client "PREMIUM" commande 2 unités du produit "P100"
    Then la commande est acceptée
    And le montant total est de 180.0 euros

  Scenario: Commande acceptée pour un client VIP
    When un client "VIP" commande 2 unités du produit "P100"
    Then la commande est acceptée
    And le montant total est de 160.0 euros
