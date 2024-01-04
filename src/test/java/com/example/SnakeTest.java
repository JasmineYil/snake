package com.example;

import at.ac.fhcampuswien.snake.ingameobjects.Position;
import at.ac.fhcampuswien.snake.ingameobjects.Snake;
import at.ac.fhcampuswien.snake.ingameobjects.Food;
import at.ac.fhcampuswien.snake.ingameobjects.Wall;
import at.ac.fhcampuswien.snake.util.Constants.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnakeTest {

    @Test
    @DisplayName("Test Initial Snake State")
    public void testInitialSnakeState() {
        Snake snake = new Snake(3, Direction.RIGHT);

        assertEquals(Direction.RIGHT, snake.getDirection());
        assertTrue(snake.isAlive());
        assertFalse(snake.isPositionUpdated());
        assertEquals(3, snake.getSegments().size());
    }

    @Test
    @DisplayName("Test Snake No Collision With Wall")
    public void testSnakeNoCollisionWithWall() {
        Snake snake = new Snake(3, Direction.RIGHT);
        Wall wall = new Wall(false, 10, 10, 5);
        snake.checkForCollisions(wall);
        assertTrue(snake.isAlive());
    }
}
