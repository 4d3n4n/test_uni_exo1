Feature: Réservation acceptée

  Une réservation est acceptée si la salle existe, que la capacité est
  suffisante, que le créneau est valide et qu'aucune réservation existante
  ne chevauche le créneau demandé.

  Background:
    Given la salle "S1" "Salle A" d'une capacité de 10 personnes

  Scenario: Réservation acceptée dans un cas nominal
    When "alice@mail.com" réserve la salle "S1" pour 5 personnes de "2026-06-10 14:00" à "2026-06-10 15:00"
    Then la réservation est acceptée

  Scenario: Réservation acceptée à la capacité maximale
    When "alice@mail.com" réserve la salle "S1" pour 10 personnes de "2026-06-10 14:00" à "2026-06-10 15:00"
    Then la réservation est acceptée

  Scenario: Réservation acceptée si le créneau commence après une réservation existante
    Given une réservation existante pour la salle "S1" de "2026-06-10 09:00" à "2026-06-10 10:00"
    When "alice@mail.com" réserve la salle "S1" pour 5 personnes de "2026-06-10 10:00" à "2026-06-10 11:00"
    Then la réservation est acceptée
