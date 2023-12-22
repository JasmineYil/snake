package at.ac.fhcampuswien.snake.board;

import at.ac.fhcampuswien.snake.ingameobjects.Food;
import at.ac.fhcampuswien.snake.ingameobjects.Position;
import at.ac.fhcampuswien.snake.ingameobjects.Snake;
import at.ac.fhcampuswien.snake.ingameobjects.Wall;
import at.ac.fhcampuswien.snake.service.HighscoreService;
import at.ac.fhcampuswien.snake.util.Player;
import at.ac.fhcampuswien.snake.util.SoundFX;
import at.ac.fhcampuswien.snake.util.StateManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static at.ac.fhcampuswien.snake.util.Constants.Direction.*;
import static at.ac.fhcampuswien.snake.util.Constants.*;

public class GameBoard {

    private static final Logger LOG = LoggerFactory.getLogger(GameBoard.class);

    private final GraphicsContext gc;

    private final Canvas gameBoardCanvas;

    /**
     * The class used for animations in the game
     */
    private final Timeline timeline;

    /**
     * This pause will be used when the snake dies and the soundFX is playing
     */
    private final PauseTransition pause = new PauseTransition(Duration.seconds(3));

    private boolean isGamePaused = false;

    private Snake snake;
    private Food regularFood;
    private Food specialFood;

    private String previousRegularFoodType;
    private String previousSpecialFoodType;

    private int foodsEatenSinceLastSpecialFood;
    private int foodsToEatUntilNextSpecialFood;

    private Wall innerWall;

    private int score;

    public int getScore() {
        return score;
    }

    private final Image snakeHeadUp;
    private final Image snakeHeadDown;
    private final Image snakeHeadLeft;
    private final Image snakeHeadRight;

    private final Image snakeBody;

    private final Image wallPattern;

    public GameBoard(Canvas gameBoardCanvas, Difficulty difficulty) {
        this.gameBoardCanvas = gameBoardCanvas;
        this.gameBoardCanvas.requestFocus();
        this.gc = gameBoardCanvas.getGraphicsContext2D();
        this.score = 0;

        this.snakeHeadUp = new Image("graphics/snake/head_up.png");
        this.snakeHeadDown = new Image("graphics/snake/head_down.png");
        this.snakeHeadLeft = new Image("graphics/snake/head_left.png");
        this.snakeHeadRight = new Image("graphics/snake/head_right.png");
        this.snakeBody = new Image("graphics/snake/body.png");
        this.wallPattern = new Image("graphics/wall/wall_pattern.png");

        timeline = new Timeline(new KeyFrame(Duration.millis(difficulty.getRefreshTime()), e -> refreshGameBoard()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * It is important to call the {@link #stopAnimation()} method when the game is over,
     * so that the timer does not continue to run in the background.
     */
    public void startGame() {
        pause.setOnFinished(e -> timeline.play());

        initializeBoardObjects();
        initializeEvents();

        gameBoardCanvas.requestFocus();

        this.score = 0;

        this.foodsEatenSinceLastSpecialFood = 0;

        StateManager.getScoreBoard().drawCountdownTimer();
        StateManager.getScoreBoard().drawScoreBoard(this.getScore());

        SoundFX.playIntroSound();

        timeline.pause();
        pause.play();
    }

    /**
     * Stops the timer which refreshes the game board.
     */
    public void stopAnimation() {
        timeline.stop();
    }

    public void endCurrentGame() {
        pause.setOnFinished(e -> {
            try {
                timeline.play();
                StateManager.switchToGameOverView();
            } catch (IOException ex) {
                LOG.error("Error switching to the GameOver view");
                ex.printStackTrace();
            }
        });

        SoundFX.playGameOverSound();

        this.stopAnimation();
        if (score != 0) {
            promptUserForInput();
            try {
                timeline.play();
                StateManager.switchToGameOverView();
            } catch (IOException ex) {
                LOG.error("Error switching to the GameOver view");
                ex.printStackTrace();
            }
        } else pause.play();
    }

    private void initializeBoardObjects() {
        snake = new Snake(INITIAL_SIZE, INITIAL_DIRECTION);
        innerWall = generateRandomWall();
        regularFood = new Food(snake, innerWall, null, false, previousRegularFoodType);
        // range 5 - 10
        foodsToEatUntilNextSpecialFood = (int) (5 + (Math.random() * 6));
        drawGameBoard(gc);
        drawWalls(gc);
        drawSnake(gc);
        drawFood(gc, regularFood);
    }

    /**
     * Generates a random wall inside the gameboard with a size smaller than 5
     * It checks the snake's position to avoid creating the wall over it
     *
     * @return a wall with random position or null if the random generated length is 0
     */
    private Wall generateRandomWall() {
        Random rand = new Random();
        int wallLength = rand.nextInt(5);
        if (wallLength == 0) return null;

        int randomX = getRandomWallPosition(rand, wallLength, true);
        int randomY = getRandomWallPosition(rand, wallLength, false);

        return new Wall(rand.nextBoolean(), randomX, randomY, wallLength);
    }

    private int getRandomWallPosition(Random rand, int wallLength, boolean isHorizontal) {
        int range = GAME_BOARD_SIZE_MEDIUM - OBJECT_SIZE_MEDIUM * (wallLength + 2);

        Set<Integer> exclusions = new HashSet<>();
        int segmentPosition;
        for (Position segment : snake.getSegments()) {
            if (isHorizontal) {
                segmentPosition = segment.getX();
            } else {
                segmentPosition = segment.getY();
            }
            exclusions.add(segmentPosition);
            for (int i = 0; i < wallLength; i++) {
                exclusions.add(segmentPosition + i * OBJECT_SIZE_MEDIUM);
                exclusions.add(segmentPosition - i * OBJECT_SIZE_MEDIUM);
            }
        }

        exclusions.add(0);
        exclusions.add(OBJECT_SIZE_MEDIUM);
        exclusions.add(OBJECT_SIZE_MEDIUM * 2);

        int random = rand.nextInt(range) / OBJECT_SIZE_MEDIUM * OBJECT_SIZE_MEDIUM;
        while (exclusions.contains(random)) {
            random = rand.nextInt(range) / OBJECT_SIZE_MEDIUM * OBJECT_SIZE_MEDIUM;
        }
        return random;
    }

    /**
     * Method to draw the checkerboard pattern on the GraphicsContext
     *
     * @param gc GraphicsContext gc used for all BoardObjects
     */
    private void drawGameBoard(GraphicsContext gc) {
        for (int i = 0; i < GAME_BOARD_SIZE_MEDIUM; i++) {
            for (int j = 0; j < GAME_BOARD_SIZE_MEDIUM; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web(GAMEBOARD_COLOR_LIGHT));
                } else {
                    gc.setFill(Color.web(GAMEBOARD_COLOR_DARK));
                }
                gc.fillRect(i * OBJECT_SIZE_MEDIUM, j * OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM);
            }
        }
    }

    private void drawFood(GraphicsContext gc, Food food) {
        Image foodImg = new Image("graphics/food/" + food.getFoodType());
        gc.drawImage(foodImg, food.getLocation().getX(), food.getLocation().getY(), OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM);
    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        Position headPosition = snake.getSegments().get(0);
        Image head = snakeHeadUp;

        switch (snake.getDirection()) {
            case RIGHT -> head = snakeHeadRight;
            case DOWN -> head = snakeHeadDown;
            case LEFT -> head = snakeHeadLeft;
        }

        gc.drawImage(head, headPosition.getX(), headPosition.getY(), OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM);

        for (int i = 1; i < snake.getSegments().size(); i++) {
            Position bodySegment = snake.getSegments().get(i);
            gc.drawImage(snakeBody, bodySegment.getX(), bodySegment.getY(), OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM);
        }
    }

    private boolean checkIfSnakeHeadIsOnFood(Food food) {
        return (snake.getSegments().get(0).getX() == food.getLocation().getX() &&
                snake.getSegments().get(0).getY() == food.getLocation().getY());
    }

    /**
     * This method centralizes the methods that draw walls in the game - the perimeter ones and walls that are inside
     * the game area and used as obstacles, if any
     *
     * @param gc the current graphics context
     */
    private void drawWalls(GraphicsContext gc) {
        drawPerimeterWalls(gc);
        if (innerWall != null) drawInnerWalls(gc);
    }

    /**
     * This method only draws the perimeter walls, without using the Wall class, to avoid overhead for collision check
     * Collision with these walls should simply be a check if the snake is outside the gameboard area, subtracting
     * the wall's thickness
     *
     * @param gc the current graphics context
     */
    private void drawPerimeterWalls(GraphicsContext gc) {
        //Upper wall
        for (int i = 0; i < GAME_BOARD_SIZE_MEDIUM; i += OBJECT_SIZE_MEDIUM) {
            gc.drawImage(wallPattern, i, 0, OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM);
        }

        //Bottom wall - we subtract 2*OBJECT_SIZE_MEDIUM from the Y position to make it visible inside the Gameboard area
        // and to account for the Menu bar
        for (int i = 0; i < GAME_BOARD_SIZE_MEDIUM; i += OBJECT_SIZE_MEDIUM) {
            gc.drawImage(wallPattern, i, GAME_BOARD_SIZE_MEDIUM - OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM);
        }

        //Left wall
        for (int i = 0; i < GAME_BOARD_SIZE_MEDIUM; i += OBJECT_SIZE_MEDIUM) {
            gc.drawImage(wallPattern, 0, i, OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM);
        }

        //Right wall - we subtract OBJECT_SIZE_MEDIUM from the X position to make it visible inside the Gameboard area
        for (int i = 0; i < GAME_BOARD_SIZE_MEDIUM; i += OBJECT_SIZE_MEDIUM) {
            gc.drawImage(wallPattern, GAME_BOARD_SIZE_MEDIUM - OBJECT_SIZE_MEDIUM, i, OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM);
        }
    }

    private void drawInnerWalls(GraphicsContext gc) {
        for (int i = 0; i < innerWall.getSegments().size(); i++) {
            Position wallSegment = innerWall.getSegments().get(i);
            gc.drawImage(wallPattern, wallSegment.getX(), wallSegment.getY(), OBJECT_SIZE_MEDIUM, OBJECT_SIZE_MEDIUM);
        }
    }

    private void initializeEvents() {
        gameBoardCanvas.setOnKeyPressed(this::checkPressedKey);
    }

    private void checkPressedKey(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> snakeMoveUp();
            case DOWN -> snakeMoveDown();
            case LEFT -> moveLeft();
            case RIGHT -> snakeMoveRight();
            case P -> pause();
            case ESCAPE -> escape();
        }
    }

    private void snakeMoveUp() {
        if (!isGamePaused && snake.getDirection() != DOWN && snake.isPositionUpdated()) {
            snake.setDirection(UP);
            snake.setPositionUpdated(false);
        }
    }

    private void snakeMoveRight() {
        if (!isGamePaused && snake.getDirection() != LEFT && snake.isPositionUpdated()) {
            snake.setDirection(RIGHT);
            snake.setPositionUpdated(false);
        }
    }

    private void snakeMoveDown() {
        if (!isGamePaused && snake.getDirection() != UP && snake.isPositionUpdated()) {
            snake.setDirection(DOWN);
            snake.setPositionUpdated(false);
        }
    }

    private void pause(){
        if (snake.isAlive()) isGamePaused = !isGamePaused;
    }

    private void moveLeft() {
        if (!isGamePaused && snake.getDirection() != RIGHT && snake.isPositionUpdated()) {
            snake.setDirection(LEFT);
            snake.setPositionUpdated(false);
        }
    }

    private void escape() {
        if (snake.isAlive()) {
            isGamePaused = true;
            Alert alert = showAlert();

            if (alert.getResult() == ButtonType.YES) {
                this.stopAnimation();

                try {
                    StateManager.switchToStartView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            this.isGamePaused = false;
        }
    }

    private Alert showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING, """
                                If you return to the Start-Screen while playing the game,
                                you will lose all points.
                                Do you really want to return to the Start-Screen?""", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        return alert;
    }

    /**
     * This method prompts the user to input their name at the end of the game.
     * This allows to keep track of recent high scores.
     */
    private void promptUserForInput() {
        String name = "";

        TextInputDialog inputPlayerName = new TextInputDialog();
        inputPlayerName.setHeaderText("Please enter your name:");
        inputPlayerName.setContentText("Name: ");

        Optional<String> result = inputPlayerName.showAndWait();
        if (result.isPresent()) {
            name = result.get().replace("%", "");
        }

        Player player = new Player(name, score);
        HighscoreService.savePlayerHighscore(player);
    }

    /**
     * The method is automatically called by a timer after n milliseconds.
     * Platform.runLater - Since we update a GUI component from a non-GUI thread, we need to put our update in a queue,
     * and it will be handled by the GUI thread as soon as possible.
     */
    private void refreshGameBoard() {
        if (isGamePaused) {
            showPauseScreen();
            return;
        }

        Platform.runLater(() -> {
            try {
                checkForFoodColision();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void showPauseScreen() {
        this.gc.setFill(Color.WHITE);
        this.gc.fillRect(OBJECT_SIZE_MEDIUM * 0.3, GAME_BOARD_SIZE_MEDIUM - OBJECT_SIZE_MEDIUM * 0.9, OBJECT_SIZE_MEDIUM * 2.7, OBJECT_SIZE_MEDIUM * 0.8);
        this.gc.setFont(new Font(OBJECT_SIZE_MEDIUM * 0.6));
        this.gc.setFill(Color.BLACK);
        this.gc.fillText("Paused!", OBJECT_SIZE_MEDIUM * 0.6, GAME_BOARD_SIZE_MEDIUM - OBJECT_SIZE_MEDIUM * 0.3, GAME_BOARD_SIZE_MEDIUM);
    }

    private void resetSpecialFoodConditions() {
        specialFood = null;
        foodsEatenSinceLastSpecialFood = 0;
        foodsToEatUntilNextSpecialFood = (int) (3 + (Math.random() * 4));
    }

    private void createRegularFood() {
        regularFood = new Food(snake, innerWall, null, false, previousRegularFoodType);
        previousRegularFoodType = regularFood.getFoodType();
    }

    private void createSpecialFood() {
        specialFood = new Food(snake, innerWall, regularFood, true, previousSpecialFoodType);
        previousSpecialFoodType = specialFood.getFoodType();
    }

    private void snakeEatsRegularFood() {
        snake.eats(regularFood);
        score += regularFood.getScoreValue();
        foodsEatenSinceLastSpecialFood++;
        StateManager.getScoreBoard().drawScoreBoard(this.getScore());
        regularFood = null;
    }

    private void snakeEatsSpecialFood() {
        snake.eats(specialFood);
        score += specialFood.getScoreValue();
        StateManager.getScoreBoard().drawScoreBoard(this.getScore());
        resetSpecialFoodConditions();
    }

    private void checkForFoodColision() {
        snake.updateSnakePosition();
        snake.checkForCollisions(innerWall);
        if (snake.isAlive()) {
            if (regularFood == null) {
                createRegularFood();
            }
            if (foodsToEatUntilNextSpecialFood == foodsEatenSinceLastSpecialFood && specialFood == null) {
                createSpecialFood();
            }
            showEntireBoard();
            checkSnakeOnFood();

        } else {
            endCurrentGame();
        }
    }

    private void checkSnakeOnFood() {
        if (checkIfSnakeHeadIsOnFood(regularFood)) {
            snakeEatsRegularFood();
        } else drawFood(gc, regularFood);
        if (specialFood != null) {
            if (checkIfSnakeHeadIsOnFood(specialFood)) {
                snakeEatsSpecialFood();
            } else {
                specialFood.decreaseSpecialFoodTimeToLive();
                if (specialFood.getSpecialFoodTimeToLive() == 0) {
                    resetSpecialFoodConditions();
                } else drawFood(gc, specialFood);
            }
        }
    }

    private void showEntireBoard(){
        gc.clearRect(0, 0, gameBoardCanvas.getWidth(), gameBoardCanvas.getHeight());
        drawGameBoard(gc);
        drawWalls(gc);
        drawSnake(gc);
    }
}
