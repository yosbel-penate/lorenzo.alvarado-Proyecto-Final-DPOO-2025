package View.UI.Menu;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Consumer;

import static com.almasb.fxgl.dsl.FXGL.*;

public class MenuController {

    private StackPane rootPane;
    private Consumer<List<String>> onGameStart;

    public MenuController(Consumer<List<String>> onGameStart) {
        this.onGameStart = onGameStart;
    }

    public void showMenu() {
        rootPane = new StackPane();
        addBackgroundImage();
        playBackgroundMusic();
        showMainMenu();
        addVersionLabel();
        getGameScene().addUINode(rootPane);
    }

    public void hideMenu() {
        rootPane.getChildren().clear();
    }

    private void addBackgroundImage() {
        Texture backgroundImage = FXGL.texture("fondo_menu.png");
        backgroundImage.setFitWidth(getAppWidth());
        backgroundImage.setFitHeight(getAppHeight());
        if (!rootPane.getChildren().contains(backgroundImage)) {
            rootPane.getChildren().add(backgroundImage);
        }
    }

    private void addVersionLabel() {
        Label versionLabel = new Label("Versión 0.0.1");
        versionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
        StackPane.setAlignment(versionLabel, Pos.BOTTOM_RIGHT);
        rootPane.getChildren().add(versionLabel);
    }

    private void showMainMenu() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Galactic Odyssey");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button startButton = createAnimatedButton("Iniciar Juego", this::showCharactersScreen);
        Button settingsButton = createAnimatedButton("Ajustes", this::showSettingsScreen);
        Button exitButton = createAnimatedButton("Salir", () -> getGameController().exit());

        menu.getChildren().addAll(title, startButton, settingsButton, exitButton);
        menu.setTranslateX(50);
        menu.setTranslateY(100);

        rootPane.getChildren().clear();
        addBackgroundImage();
        rootPane.getChildren().add(menu);
    }

    private Button createAnimatedButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setPrefSize(250, 60);
        button.setStyle("-fx-font-size: 18px; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);"
                + "-fx-text-fill: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 4, 0, 2, 2);");

        button.setOnMouseEntered(e -> button.setScaleX(1.1));
        button.setOnMouseExited(e -> button.setScaleX(1.0));

        ScaleTransition clickEffect = new ScaleTransition(Duration.millis(100), button);
        clickEffect.setToX(0.95);
        clickEffect.setToY(0.95);

        button.setOnMousePressed(e -> {
            clickEffect.setRate(1);
            clickEffect.playFromStart();
        });

        button.setOnMouseReleased(e -> {
            clickEffect.setRate(-1);
            clickEffect.playFrom("end");
            action.run();
        });

        return button;
    }

    private void showCharactersScreen() {
        CharacterSelectionController selectionController = new CharacterSelectionController(
                selectedCharacters -> showLevelSelectorScreen(selectedCharacters),
                this::showMenu
        );

        rootPane.getChildren().clear();
        addBackgroundImage();
        rootPane.getChildren().add(selectionController.getSelectionPane());
    }

    private void showLevelSelectorScreen(List<String> selectedCharacters) {
        LevelSelectorController levelSelector = new LevelSelectorController(
                this::showMenu,
                selectedCharacters,
                levelName -> {
                    onGameStart.accept(selectedCharacters);
                    hideMenu();
                }
        );

        rootPane.getChildren().clear();
        addBackgroundImage();
        rootPane.getChildren().add(levelSelector.getSelectorPane());
    }

    private void showSettingsScreen() {
        VBox settingsMenu = new VBox(20);
        settingsMenu.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Ajustes");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button backButton = createAnimatedButton("Volver", this::showMainMenu);

        settingsMenu.getChildren().addAll(title, backButton);
        settingsMenu.setTranslateX(50);
        settingsMenu.setTranslateY(100);

        rootPane.getChildren().clear();
        addBackgroundImage();
        rootPane.getChildren().add(settingsMenu);
    }

    private void playBackgroundMusic() {
        loopBGM("intro.mp3");
    }
}