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

    public boolean makeRoll() {
        throw new NotImplementedException();
    }

    public int getScore() {
        return score;
    }
}
