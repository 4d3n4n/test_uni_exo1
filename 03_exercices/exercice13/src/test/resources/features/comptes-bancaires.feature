Feature: Gestion des comptes bancaires
  Les clients doivent pouvoir creer un compte, deposer, retirer et faire des virements.

  Scenario: Creation d un nouveau compte
    Given aucun compte n existe dans l API
    When je cree un compte numero "C1" pour "Alice"
    Then la reponse HTTP doit etre 201
    And la reponse contient le solde 0

  Scenario: Depot d argent sur un compte
    Given un compte numero "C1" pour "Alice" avec un solde de 100
    When je depose 50 sur le compte "C1"
    Then la reponse HTTP doit etre 200
    And la reponse contient le solde 150

  Scenario: Retrait avec fonds suffisants
    Given un compte numero "C1" pour "Alice" avec un solde de 100
    When je retire 40 du compte "C1"
    Then la reponse HTTP doit etre 200
    And la reponse contient le solde 60

  Scenario: Retrait avec fonds insuffisants
    Given un compte numero "C1" pour "Alice" avec un solde de 30
    When je retire 100 du compte "C1"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur

  Scenario: Virement entre deux comptes
    Given un compte numero "C1" pour "Alice" avec un solde de 100
    And un compte numero "C2" pour "Bob" avec un solde de 0
    When je vire 60 du compte "C1" vers le compte "C2"
    Then la reponse HTTP doit etre 200

  Scenario: Virement refuse pour solde insuffisant
    Given un compte numero "C1" pour "Alice" avec un solde de 20
    And un compte numero "C2" pour "Bob" avec un solde de 0
    When je vire 100 du compte "C1" vers le compte "C2"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur
