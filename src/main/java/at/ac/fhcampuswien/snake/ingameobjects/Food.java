package at.ac.fhcampuswien.snake.ingameobjects;

import at.ac.fhcampuswien.snake.util.Constants;
import at.ac.fhcampuswien.snake.util.StateManager;

import java.util.Objects;
import java.util.Random;

public class Food {

    private final Position position;

    private String foodType;

    private boolean isSpecialFood;

    public int getScoreValue() {
        return scoreValue;
    }

    private int scoreValue;

    private final static int REGULAR_SCORE_VALUE = 1;
    private final static int SPECIAL_SCORE_VALUE = 3;

    private int specialFoodTimeToLive;

    private static final String[] REGULAR_FOOD_TYPES = new String[]{"strawberry.png", "lime.png", "redpepper.png", "kiwi.png", "orange.png", "tomato.png", "cherry.png", "jalapeno.png", "coco.png", "watermelon.png", "peach.png", "grapes.png", "blueberries.png", "banana.png", "chili.png"};

    private static final String[] SPECIAL_FOOD_TYPES = new String[]{"rene.png", "zayn.png", "heimo.png", "markus.png", "max.png", "leon.png"};

    public Food(Snake snake, Wall wall, Food currentlyExistingRegularFood,
                boolean isSpecialFood, String previousFoodType) {
        int scoreValueMultiplierBasedOnDifficulty = getDifficulty();
        if (isSpecialFood) {
            this.isSpecialFood = true;
            this.scoreValue = SPECIAL_SCORE_VALUE * scoreValueMultiplierBasedOnDifficulty;
            // range: 18 - 36
            this.specialFoodTimeToLive = (int) (18 + (Math.random() * 18));
            do {
                int foodTypeNumber = (int) (Math.random() * SPECIAL_FOOD_TYPES.length);
                this.foodType = SPECIAL_FOOD_TYPES[foodTypeNumber];
            } while (Objects.equals(foodType, previousFoodType));
        } else {
            this.scoreValue = REGULAR_SCORE_VALUE * scoreValueMultiplierBasedOnDifficulty;
            this.specialFoodTimeToLive = -1;
            do {
                int foodTypeNumber = (int) (Math.random() * REGULAR_FOOD_TYPES.length);
                foodType = REGULAR_FOOD_TYPES[foodTypeNumber];
            } while (Objects.equals(foodType, previousFoodType));
        }
        boolean isTargetFieldFree;
        int foodXCoord;
        int foodYCoord;
        // We reduce "2" from segmentNumberX, because there are two Colums being used for the outer walls.
        int segmentNumberX = Constants.NUMBER_OF_ROWS_AND_COLS - 2;
        // We reduce "3" from segmentNumberY, because there are two rows being used for the outer walls.
        // Additionally, there is 1 row used for the status bar at the top.
        int segmentNumberY = Constants.NUMBER_OF_ROWS_AND_COLS - 3;
        do {
            isTargetFieldFree = true;
            foodXCoord = (int) (Math.random() * segmentNumberX) + 1;
            foodYCoord = (int) (Math.random() * segmentNumberY) + 1;

            // Since the Location of the Elements of the Snake is in PX, we need to multiply
            // the row and column number by the Object Size in PX.
            foodXCoord *= Constants.OBJECT_SIZE_MEDIUM;
            foodYCoord *= Constants.OBJECT_SIZE_MEDIUM;

            isTargetFieldFree = checkIfPositionIsInhibitedByTheSnake(snake, foodXCoord, foodYCoord);

            isTargetFieldFree = checkIfPositionIsInhibitedByTheWall(wall, foodXCoord, foodYCoord);
            // Check if currently existing regular Food is on desired Position
            isTargetFieldFree = checkIfSpecialFoodIsOnPosition(currentlyExistingRegularFood, isSpecialFood, foodXCoord, foodYCoord);
        } while (!isTargetFieldFree);
        position = new Position(foodXCoord, foodYCoord);
    }

    private boolean checkIfPositionIsInhibitedByTheWall(Wall wall, int foodXPosition, int foodYPosition) {
        for (int i = 0; i < wall.getSegments().size(); i++) {
            if (wall.getSegments().get(i).getX() == foodXPosition && wall.getSegments().get(i).getY() == foodYPosition)
                return false;
        }
        return true;
    }

    private boolean checkIfSpecialFoodIsOnPosition(Food currentlyExistingRegularFood, boolean isSpecialFood, int foodXPosition, int foodYPosition) {
        if (isSpecialFood
                && currentlyExistingRegularFood != null
                && (currentlyExistingRegularFood.position.getX() == foodXPosition
                && currentlyExistingRegularFood.position.getY() == foodYPosition)) {
            return false;
        }
        return true;
    }

    private boolean checkIfPositionIsInhibitedByTheSnake(Snake snake, int foodXPosition, int foodYPosition) {
        for (int i = 0; i < snake.getSegments().size(); i++) {
            if (snake.getSegments().get(i).getX() == foodXPosition
                    && snake.getSegments().get(i).getY() == foodYPosition) {
                return false;
            }
        }
        return true;
    }

    private int getDifficulty() {
        switch (StateManager.getDifficulty()) {
            case EASY -> {return 1;}
            case MEDIUM -> {return 2;}
            case HARD -> {return 3;}
            default -> throw new IllegalStateException("Unexpected value: " + StateManager.getDifficulty());
        }
    }

    public Position getLocation() {
        return position;
    }

    public String getFoodType() {
        return foodType;
    }

    public boolean isSpecialFood() {
        return isSpecialFood;
    }

    public int getSpecialFoodTimeToLive() {
        return specialFoodTimeToLive;
    }

    public void decreaseSpecialFoodTimeToLive() {
        specialFoodTimeToLive--;
    }
}