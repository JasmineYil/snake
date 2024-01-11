package com.example;

import at.ac.fhcampuswien.snake.board.ScoreBoard;
import at.ac.fhcampuswien.snake.util.Constants;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ScoreBoardTest {

    private ScoreBoard scoreBoard;
    private Canvas canvasMock;
    private GraphicsContext graphicsContextMock;


    @BeforeEach
    public void setup() {
        canvasMock = mock(Canvas.class);
        graphicsContextMock = mock(GraphicsContext.class);

        when(canvasMock.getGraphicsContext2D()).thenReturn(graphicsContextMock);

        scoreBoard = new ScoreBoard(canvasMock);
    }

    @Test
    public void drawScoreBoard_ValidScore_ScoreDisplayedCorrectly() {
        // Arrange
        int score = 50;

        // Act
        scoreBoard.drawScoreBoard(score);

        // Assert
        verify(graphicsContextMock).setFill(Color.web("4a148c"));
        verify(graphicsContextMock).fillRect(0, 0, Constants.SCOREBOARD_WIDTH, Constants.SCOREBOARD_HEIGHT);
        verify(graphicsContextMock).setTextAlign(TextAlignment.RIGHT);
        verify(graphicsContextMock).setTextBaseline(VPos.CENTER);
        verify(graphicsContextMock).setFont(Font.font("Courier", Constants.OBJECT_SIZE_MEDIUM));
        verify(graphicsContextMock).setFill(Color.WHITE);
        verify(graphicsContextMock).fillText("Score: " + score, Constants.SCOREBOARD_WIDTH - 7d, Constants.SCOREBOARD_HEIGHT / 2f);
    }


}