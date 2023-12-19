package at.ac.fhcampuswien.snake.board;

import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static at.ac.fhcampuswien.snake.util.Constants.*;

public class ScoreBoard {
    private final GraphicsContext gc;

    public ScoreBoard(Canvas scoreBoardCanvas) {
        this.gc = scoreBoardCanvas.getGraphicsContext2D();
    }

    public void drawScoreBoard(int score) {
        String purpleHexColor = "4a148c";
        this.gc.setFill(Color.web(purpleHexColor));
        this.gc.fillRect(0, 0, SCOREBOARD_WIDTH, SCOREBOARD_HEIGHT);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(VPos.CENTER);
        this.gc.setFill(Color.WHITE);
        this.gc.setFont(Font.font("Courier", OBJECT_SIZE_MEDIUM));
        this.gc.fillText("Score: " + score, SCOREBOARD_WIDTH - 7d, SCOREBOARD_HEIGHT / 2f);
    }

    public void drawCountdownTimer() {
        String purpleHexColor = "4a148c";
        int duration = 3;
        Text timerText = new Text(10, 20, Integer.toString(duration));

        Thread timerThread = new Thread(() -> {
            for (int i = duration; i >= 0; i--) {
                int finalI = i;
                Platform.runLater(() -> {
                    this.gc.setFill(Color.web(purpleHexColor));
                    this.gc.fillRect(0, 0, SCOREBOARD_WIDTH / 2f, SCOREBOARD_HEIGHT);
                    if (finalI > 0) {
                        this.gc.setTextAlign(TextAlignment.LEFT);
                        this.gc.setTextBaseline(VPos.CENTER);
                        this.gc.setFill(Color.WHITE);
                        this.gc.setFont(Font.font("Courier", OBJECT_SIZE_MEDIUM));

                        timerText.setText("Starting in: " + finalI);
                        this.gc.fillText(timerText.getText(), 7, SCOREBOARD_HEIGHT / 2f);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });

        timerThread.start();
    }

}
