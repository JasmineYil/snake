package com.example;

import at.ac.fhcampuswien.snake.ingameobjects.Position;
import at.ac.fhcampuswien.snake.ingameobjects.Wall;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static at.ac.fhcampuswien.snake.util.Constants.OBJECT_SIZE_MEDIUM;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


class WallTest {

    @Test
    @DisplayName("Check if Horizontal wall is correctly created")
    void testHorizontalWallCreation() {
        // Arrange
        boolean isHorizontal = true;
        int startingPositionX = 5;
        int startingPositionY = 10;
        int length = 3;

        // Act
        Wall wall = new Wall(isHorizontal, startingPositionX, startingPositionY, length);
        List<Position> segments = wall.getSegments();

        // Assert
        assertEquals(length, segments.size());
        for (int i = 0; i < length; i++) {
            assertEquals(startingPositionX + i * OBJECT_SIZE_MEDIUM, segments.get(i).getX());
            assertEquals(startingPositionY, segments.get(i).getY());
        }
    }

    @Test
    @DisplayName("Check if Vertical wall is correctly created")
    void testVerticalWallCreation() {
        // Arrange
        boolean isHorizontal = false;
        int startingPositionX = 8;
        int startingPositionY = 15;
        int length = 4;

        // Act
        Wall wall = new Wall(isHorizontal, startingPositionX, startingPositionY, length);
        List<Position> segments = wall.getSegments();

        // Assert
        assertEquals(length, segments.size());
        for (int i = 0; i < length; i++) {
            assertEquals(startingPositionX, segments.get(i).getX());
            assertEquals(startingPositionY + i * OBJECT_SIZE_MEDIUM, segments.get(i).getY());
        }
    }
}
