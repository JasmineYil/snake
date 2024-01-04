package com.example;

import at.ac.fhcampuswien.snake.ingameobjects.Food;
import at.ac.fhcampuswien.snake.ingameobjects.Snake;
import at.ac.fhcampuswien.snake.ingameobjects.Wall;
import at.ac.fhcampuswien.snake.util.Constants.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodTest {

    @Test
    @DisplayName("Regular Food Creation Should Succeed")
    void regularFoodCreationShouldSucceed() {
        // Arrange
        Snake snake = new Snake(3, Direction.RIGHT);  // Initial snake with 3 segments
        Wall wall = new Wall(false, 5, 5, 3);  // Vertical wall starting at (5,5) with length 3
        Food previousFood = null;

        // Act
        Food food = new Food(snake, wall, previousFood, false, "");

        // Assert
        assertNotNull(food);
        assertFalse(food.isSpecialFood());
        assertNotNull(food.getFoodType());
        assertNotNull(food.getLocation());
        assertTrue(food.getScoreValue() > 0);
    }

    @Test
    @DisplayName("Special Food Creation Should Succeed")
    void specialFoodCreationShouldSucceed() {
        // Arrange
        Snake snake = new Snake(3, Direction.RIGHT);  // Initial snake with 3 segments
        Wall wall = new Wall(false, 5, 5, 3);  // Vertical wall starting at (5,5) with length 3
        Food previousFood = null;

        // Act
        Food food = new Food(snake, wall, previousFood, true, "");

        // Assert
        assertNotNull(food);
        assertTrue(food.isSpecialFood());
        assertNotNull(food.getFoodType());
        assertNotNull(food.getLocation());
        assertTrue(food.getScoreValue() > 0);
        assertTrue(food.getSpecialFoodTimeToLive() >= 18 && food.getSpecialFoodTimeToLive() <= 36);
    }

    @Test
    @DisplayName("New Food Should Not Collide With Snake")
    void newFoodShouldNotCollideWithSnake() {
        // Arrange
        Snake snake = new Snake(3, Direction.RIGHT);  // Initial snake with 3 segments
        Wall wall = new Wall(false, 5, 5, 3);  // Vertical wall starting at (5,5) with length 3
        Food previousFood = null;
        Food food = new Food(snake, wall, previousFood, false, "");

        // Act
        snake.getSegments().get(0).setPosition(food.getLocation());
        Food newFood = new Food(snake, wall, previousFood, false, "");

        // Assert
        assertNotEquals(food.getLocation(), newFood.getLocation());
    }

    @Test
    @DisplayName("New Food Should Not Collide With Wall")
    void newFoodShouldNotCollideWithWall() {
        // Arrange
        Snake snake = new Snake(3, Direction.RIGHT);  // Initial snake with 3 segments
        Wall wall = new Wall(false, 5, 5, 3);  // Vertical wall starting at (5,5) with length 3
        Food previousFood = null;
        Food food = new Food(snake, wall, previousFood, false, "");

        // Act
        wall.getSegments().get(0).setPosition(food.getLocation());
        Food newFood = new Food(snake, wall, previousFood, false, "");

        // Assert
        assertNotEquals(food.getLocation(), newFood.getLocation());
    }

    @Test
    @DisplayName("New Food Should Not Collide With Previous Food")
    void newFoodShouldNotCollideWithPreviousFood() {
        // Arrange
        Snake snake = new Snake(3, Direction.RIGHT);  // Initial snake with 3 segments
        Wall wall = new Wall(false, 5, 5, 3);  // Vertical wall starting at (5,5) with length 3
        Food previousFood = new Food(snake, wall, null, false, "");
        Food food = new Food(snake, wall, previousFood, false, "");

        // Act
        previousFood.getLocation().setPosition(food.getLocation());
        Food newFood = new Food(snake, wall, previousFood, false, "");

        // Assert
        assertNotEquals(food.getLocation(), newFood.getLocation());
    }
}
