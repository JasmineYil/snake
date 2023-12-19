package at.ac.fhcampuswien.snake.ingameobjects;

import java.util.ArrayList;
import java.util.List;

import static at.ac.fhcampuswien.snake.util.Constants.OBJECT_SIZE_MEDIUM;

public class Wall {
    private final List<Position> segments = new ArrayList<>();

    public Wall(boolean isHorizontal, int startingPositionX, int startingPositionY, int length) {
        if (isHorizontal) {
            for (int i = 0; i < length; i++) {
                segments.add(new Position(startingPositionX + i * OBJECT_SIZE_MEDIUM, startingPositionY));
            }
        } else {
            for (int i = 0; i < length; i++) {
                segments.add(new Position(startingPositionX, startingPositionY + i * OBJECT_SIZE_MEDIUM));
            }
        }
    }

    public List<Position> getSegments() {
        return segments;
    }
}
