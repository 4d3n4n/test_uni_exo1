package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une série (frame) du jeu de bowling.
 * Phase RED : makeRoll() n'est pas encore implémentée.
 */
public class Frame {
    private int score;
    private boolean lastFrame;
    private IGenerateur generateur;
    private List<Roll> rolls = new ArrayList<>();

    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
    }

    /**
     * Effectue un lancer si la série l'autorise encore.
     * Le nombre de quilles tombées est fourni par le générateur (mocké dans les tests).
     *
     * @return true si le lancer a été effectué, false s'il est refusé.
     */
    public boolean makeRoll() {
        if (!canRoll()) {
            return false;
        }
        int pins = generateur.randomPin(maxPinsForNextRoll());
        rolls.add(new Roll(pins));
        score += pins;
        return true;
    }

    /**
     * Détermine si un nouveau lancer est autorisé selon le type de série.
     */
    private boolean canRoll() {
        int count = rolls.size();
        if (!lastFrame) {
            // Série standard : 2 lancers max, et aucun second lancer après un strike.
            if (count == 0) {
                return true;
            }
            if (count == 1) {
                return !isStrike();
            }
            return false;
        }
        // Série finale : 3e lancer autorisé uniquement après un strike ou un spare.
        if (count < 2) {
            return true;
        }
        if (count == 2) {
            return isStrike() || isSpare();
        }
        return false;
    }

    // isStrike() n'est appelée que lorsque la série contient au moins un lancer (cf. canRoll()).
    private boolean isStrike() {
        return rolls.get(0).getPins() == 10;
    }

    // isSpare() n'est évaluée que pour une série finale comptant 2 lancers sans strike (cf. canRoll()).
    private boolean isSpare() {
        return rolls.get(0).getPins() + rolls.get(1).getPins() == 10;
    }

    /**
     * Nombre de quilles encore debout pour le prochain lancer.
     * Après un strike ou un spare (série finale), un nouveau jeu de quilles est dressé.
     */
    private int maxPinsForNextRoll() {
        int standing = 0;
        for (Roll roll : rolls) {
            standing += roll.getPins();
            if (standing == 10) {
                standing = 0; // strike ou spare → quilles redressées
            }
        }
        return 10 - standing;
    }

    public int getScore() {
        return score;
    }
}
