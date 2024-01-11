package com.example;

import at.ac.fhcampuswien.snake.*;

import at.ac.fhcampuswien.snake.board.GameBoard;
import at.ac.fhcampuswien.snake.util.Constants;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)

class GameBoardTest {
    @Mock
    private Canvas mockCanvas;
    @Mock
    private GraphicsContext mockGraphicsContext;
    @Mock
    private Image mockImage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockCanvas.getGraphicsContext2D()).thenReturn(mockGraphicsContext);
        when(mockGraphicsContext.getCanvas()).thenReturn(mockCanvas);
    }
//
//    @Test
//    void constructorShouldInitializeGameBoardInstance() {
//        // Arrange
//        Constants.Difficulty difficulty = Constants.Difficulty.MEDIUM;
//
//        // Act
//        GameBoard gameBoard = new GameBoard(mockCanvas, difficulty);
//
//        // Assert
//        assertNotNull(gameBoard);
//        assertNotNull(gameBoard.getGameBoardCanvas());
//        assertNotNull(gameBoard.getGraphicsContext());
//        assertNotNull(gameBoard.getTimeline());
//        assertNotNull(gameBoard.getSnakeHeadUp());
//        assertNotNull(gameBoard.getSnakeHeadDown());
//        assertNotNull(gameBoard.getSnakeHeadLeft());
//        assertNotNull(gameBoard.getSnakeHeadRight());
//        assertNotNull(gameBoard.getSnakeBody());
//        assertNotNull(gameBoard.getWallPattern());
//        assertEquals(0, gameBoard.getScore());
//
//        // Check if the timeline is properly initialized (not null)
//        assertNotNull(gameBoard.getTimeline());
//
//        // Check the refresh time and cycle count
//        assertEquals(difficulty.getRefreshTime(), gameBoard.getTimeline().getKeyFrames().get(0).getTime().toMillis());
//        assertEquals(Animation.INDEFINITE, gameBoard.getTimeline().getCycleCount());
//    }
}
