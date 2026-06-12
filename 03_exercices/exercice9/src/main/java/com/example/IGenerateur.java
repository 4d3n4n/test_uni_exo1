package com.example;

/**
 * Source d'aléatoire pour un lancer de bowling.
 * Isolée derrière une interface afin de pouvoir la mocker (Mockito)
 * et contrôler le nombre de quilles tombées à chaque lancer dans les tests.
 */
public interface IGenerateur {
    int randomPin(int max);
}
