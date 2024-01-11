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
    @Test
    @DisplayName("Test Snake Collision With Game Border")
    public void testSnakeCollisionWithGameBorder() {
        Snake snake = new Snake(3, Direction.UP);
        snake.getSegments().get(0).setX(0);
        snake.getSegments().get(0).setY(0);
        snake.checkForCollisions(null);
        assertFalse(snake.isAlive());
    }
    @Test
    @DisplayName("Test Snake Collision With Itself")
    public void testSnakeCollisionWithItself() {
        Snake snake = new Snake(3, Direction.RIGHT);
        snake.getSegments().get(0).setX(10);
        snake.getSegments().get(0).setY(10);
        snake.getSegments().get(1).setX(10);
        snake.getSegments().get(1).setY(20);
        snake.getSegments().get(2).setX(10);
        snake.getSegments().get(2).setY(30);
        snake.checkForCollisions(null);
        assertFalse(snake.isAlive());
    }
    @Test
    @DisplayName("Test Snake Eats Food")
    public void testSnakeAteRegularFood() {

    }

}
