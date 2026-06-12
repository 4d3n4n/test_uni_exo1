Feature: Recherche de produits

  En tant qu'utilisateur, je veux rechercher des produits pour trouver rapidement ce dont j'ai besoin.

  Background:
    Given le catalogue contient les produits :
      | nom             | categorie    | prix |
      | Clavier USB     | Informatique | 25   |
      | Souris sans fil | Informatique | 40   |
      | Casque audio    | Audio        | 80   |

  Scenario: Recherche par mot-clé
    When l'utilisateur recherche les produits contenant "Clavier"
    Then les résultats contiennent "Clavier USB"
    And les résultats ne contiennent pas "Casque audio"

  Scenario: Recherche par prix maximum
    When l'utilisateur recherche les produits à un prix maximum de 40
    Then les résultats contiennent "Clavier USB"
    And les résultats contiennent "Souris sans fil"
    And les résultats ne contiennent pas "Casque audio"
