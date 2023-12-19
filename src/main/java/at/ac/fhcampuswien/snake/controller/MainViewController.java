package at.ac.fhcampuswien.snake.controller;

import at.ac.fhcampuswien.snake.util.Constants;
import at.ac.fhcampuswien.snake.util.StateManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

    protected Stage stage = null;
    @FXML
    private Label textBox;

    @FXML
    private ImageView logo;

    @FXML
    private VBox body;

    @FXML
    private ComboBox comboBox;

    @FXML
    private VBox footer;


    public void initialize() {
        this.logo.setImage(new Image("graphics/snake_logo.jpg"));
        this.logo.setPreserveRatio(true);
        this.logo.setSmooth(true);
        this.textBox.setText("\n\n");

        VBox.setVgrow(footer, javafx.scene.layout.Priority.ALWAYS);
        footer.setAlignment(Pos.BOTTOM_CENTER);
    }

    @FXML
    public void startGame() throws IOException {
        String difficulty = comboBox.getValue().toString();
        switch (difficulty) {
            case "Easy" -> StateManager.setDifficulty(Constants.Difficulty.EASY);
            case "Medium" -> StateManager.setDifficulty(Constants.Difficulty.MEDIUM);
            case "Hard" -> StateManager.setDifficulty(Constants.Difficulty.HARD);
        }
        StateManager.switchToGameView();
    }

    @FXML
    public void showInfoDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText(
                """
                        Awesome Snake game
                        Courtesy of PurplePain™
                        """);
        alert.showAndWait();
    }

    @FXML
    public void onExitButtonClick() {
        Platform.exit();
    }

    public ImageView getLogo() {
        return this.logo;
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }
}