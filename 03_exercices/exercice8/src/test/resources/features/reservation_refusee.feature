Feature: Réservation refusée

  Une réservation est refusée si la salle n'existe pas, si le nombre de
  participants dépasse la capacité, si la période est invalide (fin <= début)
  ou si la salle est déjà réservée sur le créneau demandé.

  Scenario: Réservation refusée si la salle est inconnue
    Given la salle "S9" n'existe pas
    When "alice@mail.com" réserve la salle "S9" pour 5 personnes de "2026-06-10 14:00" à "2026-06-10 15:00"
    Then la réservation est refusée avec le message "Salle inconnue"

  Scenario: Réservation refusée si la capacité est insuffisante
    Given la salle "S1" "Salle A" d'une capacité de 10 personnes
    When "alice@mail.com" réserve la salle "S1" pour 15 personnes de "2026-06-10 14:00" à "2026-06-10 15:00"
    Then la réservation est refusée avec le message "Capacité insuffisante"

  Scenario: Réservation refusée si la période est invalide
    Given la salle "S1" "Salle A" d'une capacité de 10 personnes
    When "alice@mail.com" réserve la salle "S1" pour 5 personnes de "2026-06-10 15:00" à "2026-06-10 14:00"
    Then la réservation est refusée avec le message "Période invalide"

  Scenario: Réservation refusée si la salle est déjà réservée sur le créneau
    Given la salle "S1" "Salle A" d'une capacité de 10 personnes
    And une réservation existante pour la salle "S1" de "2026-06-10 14:00" à "2026-06-10 16:00"
    When "alice@mail.com" réserve la salle "S1" pour 5 personnes de "2026-06-10 15:00" à "2026-06-10 17:00"
    Then la réservation est refusée avec le message "Salle déjà réservée"
