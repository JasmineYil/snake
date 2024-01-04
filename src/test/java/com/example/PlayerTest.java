
package com.example;

import at.ac.fhcampuswien.snake.util.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Player class")
public class PlayerTest {

    @Test
    @DisplayName("Test getName() method")
    void testGetName() {
        // Arrange
        String playerName = "Alice";
        Player player = new Player(playerName, 100);

        // Act
        String retrievedName = player.getName();

        // Assert
        assertEquals(playerName, retrievedName, "getName() should return the player's name");
    }

    @Test
    @DisplayName("Test getScore() method")
    void testGetScore() {
        // Arrange
        int initialScore = 150;
        Player player = new Player("Bob", initialScore);

        // Act
        int retrievedScore = player.getScore();

        // Assert
        assertEquals(initialScore, retrievedScore, "getScore() should return the player's score");
    }

    @Test
    @DisplayName("Test initial score is zero")
    void testInitialScoreZero() {
        // Arrange
        int initialScore = 0;
        Player player = new Player("Charlie", initialScore);

        // Act
        int retrievedScore = player.getScore();

        // Assert
        assertEquals(initialScore, retrievedScore, "New player's score should be initialized as 0");
    }
}

