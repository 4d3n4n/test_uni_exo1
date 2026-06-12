package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Tests TDD de la classe Frame.
 * Le générateur d'aléatoire est mocké : on impose les quilles tombées
 * à chaque lancer via when(generateur.randomPin(anyInt())).thenReturn(...).
 */
@ExtendWith(MockitoExtension.class)
class FrameTest {

    @Mock
    private IGenerateur generateur;

    // ---------- Série standard (round courant, lastFrame = false) ----------

    @Test
    void shouldIncreaseScoreWhenFirstRollIsMadeInStandardFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(4);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();

        assertEquals(4, frame.getScore());
    }

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeInStandardFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(4, 3);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();
        frame.makeRoll();

        assertEquals(7, frame.getScore());
    }

    @Test
    void shouldRejectSecondRollWhenStandardFrameStartsWithStrike() {
        when(generateur.randomPin(anyInt())).thenReturn(10);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();

        assertFalse(frame.makeRoll());
    }

    @Test
    void shouldRejectThirdRollWhenStandardFrameAlreadyHasTwoRolls() {
        when(generateur.randomPin(anyInt())).thenReturn(3, 4);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();
        frame.makeRoll();

        assertFalse(frame.makeRoll());
    }

    // ---------- Série finale (lastFrame = true) ----------

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeAfterStrikeInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();

        assertEquals(15, frame.getScore());
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithStrike() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();

        assertTrue(frame.makeRoll());
    }

    @Test
    void shouldAcceptSecondRollWhenLastFrameStartsWithStrike() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();

        assertTrue(frame.makeRoll());
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterStrikeInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 3, 4);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        assertEquals(17, frame.getScore());
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithSpare() {
        when(generateur.randomPin(anyInt())).thenReturn(6, 4, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();

        assertTrue(frame.makeRoll());
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterSpareInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(6, 4, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        assertEquals(15, frame.getScore());
    }

    @Test
    void shouldRejectThirdRollWhenLastFrameHasNoStrikeOrSpare() {
        when(generateur.randomPin(anyInt())).thenReturn(3, 4);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();

        assertFalse(frame.makeRoll());
    }

    @Test
    void shouldRejectFourthRollInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        assertFalse(frame.makeRoll());
    }
}
