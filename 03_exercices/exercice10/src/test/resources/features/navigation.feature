Feature: Navigation par catégorie

  En tant qu'utilisateur, je veux naviguer par catégorie de produits pour découvrir ce qui est disponible.

  Background:
    Given le catalogue contient les produits :
      | nom          | categorie    | prix |
      | Clavier USB  | Informatique | 25   |
      | Casque audio | Audio        | 80   |

  Scenario: Sélection d'une catégorie
    When l'utilisateur sélectionne la catégorie "Informatique"
    Then les résultats contiennent "Clavier USB"
    And les résultats ne contiennent pas "Casque audio"
