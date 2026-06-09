Feature: Notification de réservation

  Une notification de confirmation est envoyée lorsque la réservation est
  acceptée, et aucune notification n'est envoyée en cas de refus.

  Scenario: Notification envoyée en cas de succès
    Given la salle "S1" "Salle A" d'une capacité de 10 personnes
    When "alice@mail.com" réserve la salle "S1" pour 5 personnes de "2026-06-10 14:00" à "2026-06-10 15:00"
    Then la réservation est acceptée
    And une notification de confirmation est envoyée à "alice@mail.com"

  Scenario: Notification non envoyée en cas d'échec
    Given la salle "S9" n'existe pas
    When "alice@mail.com" réserve la salle "S9" pour 5 personnes de "2026-06-10 14:00" à "2026-06-10 15:00"
    Then la réservation est refusée avec le message "Salle inconnue"
    And aucune notification n'est envoyée
