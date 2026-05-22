package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiceScoreTest {

    @Mock
    private Ide de;

    @Test
    void shouldReturn22WhenBothDiceAre6() {
        when(de.getRoll()).thenReturn(6, 6);
        DiceScore diceScore = new DiceScore(de);
        assertEquals(30, diceScore.getScore());
    }

    @Test
    void shouldReturnDoubleValuePlus10WhenBothDiceAreEqual() {
        when(de.getRoll()).thenReturn(3, 3);
        DiceScore diceScore = new DiceScore(de);
        assertEquals(16, diceScore.getScore());
    }

    @Test
    void shouldReturnHighestValueWhenDiceAreDifferent() {
        when(de.getRoll()).thenReturn(2, 5);
        DiceScore diceScore = new DiceScore(de);
        assertEquals(5, diceScore.getScore());
    }
}
