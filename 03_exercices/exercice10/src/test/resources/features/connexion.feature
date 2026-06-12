Feature: Connexion

  En tant qu'utilisateur, je veux me connecter à mon compte pour passer des commandes.

  Background:
    Given un compte existe avec le nom d'utilisateur "alice" et le mot de passe "secret"

  Scenario: Connexion réussie
    When "alice" se connecte avec le mot de passe "secret"
    Then la connexion réussit et l'utilisateur est redirigé vers la page d'accueil

  Scenario: Connexion échouée avec un mauvais mot de passe
    When "alice" se connecte avec le mot de passe "wrong"
    Then la connexion échoue avec un message d'erreur
