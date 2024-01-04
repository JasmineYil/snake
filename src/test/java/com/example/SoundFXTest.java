package com.example;

import at.ac.fhcampuswien.snake.util.SoundFX;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("SoundFX Test")
class SoundFXTest {

    @Test
    @DisplayName("Play Intro Sound Test")
    void testPlayIntroSound() {
        assertDoesNotThrow(SoundFX::playIntroSound);
    }

    @Test
    @DisplayName("Play Eating Sound Test")
    void testPlayEatingSound() {
        assertDoesNotThrow(SoundFX::playEatingSound);
    }

    @Test
    @DisplayName("Play Bonus Point Sound Test")
    void testPlayBonusPointSound() {
        assertDoesNotThrow(SoundFX::playBonusPointSound);
    }

    @Test
    @DisplayName("Play Game Over Sound Test")
    void testPlayGameOverSound() {
        assertDoesNotThrow(SoundFX::playGameOverSound);
    }
}
