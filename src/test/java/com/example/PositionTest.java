package com.example;

import at.ac.fhcampuswien.snake.ingameobjects.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    @DisplayName("Position Initialization Should Succeed")
    void positionInitializationShouldSucceed() {
        // Arrange
        int x = 5;
        int y = 10;

        // Act
        Position position = new Position(x, y);

        // Assert
        assertNotNull(position);
        assertEquals(x, position.getX());
        assertEquals(y, position.getY());
    }

    @Test
    @DisplayName("GetPositionX Should Return Correct X Coordinate")
    void getPositionXShouldReturnCorrectXCoordinate() {
        // Arrange
        int x = 5;
        int y = 10;
        Position position = new Position(x, y);

        // Act
        int result = position.getX();

        // Assert
        assertEquals(x, result);
    }

    @Test
    @DisplayName("GetPositionY Should Return Correct Y Coordinate")
    void getPositionYShouldReturnCorrectYCoordinate() {
        // Arrange
        int x = 5;
        int y = 10;
        Position position = new Position(x, y);

        // Act
        int result = position.getY();

        // Assert
        assertEquals(y, result);
    }
}

