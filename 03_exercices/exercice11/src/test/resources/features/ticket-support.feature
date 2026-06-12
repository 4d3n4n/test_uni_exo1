Feature: Gestion des tickets de support
  Les utilisateurs du support doivent pouvoir creer, consulter et resoudre des tickets.

  Scenario: Creation d un ticket valide
    Given aucun ticket n existe dans l API
    When je cree un ticket avec le titre "Panne serveur" et la priorite "HIGH"
    Then la reponse HTTP doit etre 201
    And la reponse contient le statut "OPEN"

  Scenario: Resolution d un ticket
    Given un ticket ouvert existe avec le titre "Incident reseau"
    When je passe le ticket au statut "RESOLVED"
    Then la reponse HTTP doit etre 200
    And la reponse contient le statut "RESOLVED"

  Scenario: Refus de modification d un ticket deja resolu
    Given un ticket resolu existe avec le titre "Incident clos"
    When je passe le ticket au statut "IN_PROGRESS"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur

  Scenario: Consultation d un ticket inexistant
    Given aucun ticket n existe dans l API
    When je demande le ticket avec l identifiant 99
    Then la reponse HTTP doit etre 404
    And la reponse contient un message d erreur
