Feature: Création de compte

  En tant qu'utilisateur, je veux créer un compte pour pouvoir passer des commandes.

  Scenario: Inscription réussie
    Given aucun compte n'existe pour le nom d'utilisateur "alice"
    When l'utilisateur s'inscrit avec l'email "alice@mail.com", le nom d'utilisateur "alice" et le mot de passe "secret"
    Then l'inscription est confirmée pour "alice"

  Scenario: Inscription refusée si le nom d'utilisateur existe déjà
    Given un compte existe déjà pour le nom d'utilisateur "alice"
    When l'utilisateur s'inscrit avec l'email "alice2@mail.com", le nom d'utilisateur "alice" et le mot de passe "secret"
    Then l'inscription est refusée
