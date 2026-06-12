Feature: Reservation de salles de reunion
  Les utilisateurs doivent pouvoir reserver une salle libre sur un creneau donne.

  Scenario: Reservation acceptee quand la salle existe et le creneau est libre
    Given une salle "Salle A" d une capacite de 10 existe
    When je reserve la salle existante pour "Alice" de "2026-06-10T14:00" a "2026-06-10T15:00"
    Then la reponse HTTP doit etre 201
    And la reponse contient le statut "CONFIRMED"

  Scenario: Reservation refusee quand la salle n existe pas
    Given aucune salle n existe dans l API
    When je reserve la salle 999 pour "Alice" de "2026-06-10T14:00" a "2026-06-10T15:00"
    Then la reponse HTTP doit etre 404
    And la reponse contient un message d erreur

  Scenario: Reservation refusee quand le creneau chevauche une reservation existante
    Given une salle "Salle A" d une capacite de 10 existe
    And une reservation confirmee existe pour cette salle de "2026-06-10T14:00" a "2026-06-10T15:00"
    When je reserve la salle existante pour "Bob" de "2026-06-10T14:30" a "2026-06-10T15:30"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur
