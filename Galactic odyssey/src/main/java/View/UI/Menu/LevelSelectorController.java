package View.UI.Menu;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.almasb.fxgl.dsl.FXGL.*;

public class LevelSelectorController {

    private StackPane selectorPane;
    private Runnable onBackToMainMenu;
    private Button playButton;
    private String selectedLevel;
    private List<String> selectedCharacters;
    private Consumer<String> onLevelSelected;

    private static class LevelData {
        String name;
        String imagePath;
        String description;

        public LevelData(String name, String imagePath, String description) {
            this.name = name;
            this.imagePath = imagePath;
            this.description = description;
        }
    }

    public LevelSelectorController(Runnable onBackToMainMenu, List<String> selectedCharacters, Consumer<String> onLevelSelected) {
        this.onBackToMainMenu = onBackToMainMenu;
        this.selectedCharacters = selectedCharacters;
        this.onLevelSelected = onLevelSelected;
        this.selectorPane = new StackPane();
        this.selectedLevel = null;
        initializeUI();
    }

    private void initializeUI() {
        Rectangle bg = new Rectangle(getAppWidth(), getAppHeight(), Color.rgb(0, 0, 0, 0.85));
        selectorPane.getChildren().add(bg);
        showLevelSelection();
    }

    private void showLevelSelection() {
        VBox mainContainer = new VBox(15);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setMaxWidth(getAppWidth() * 0.9);
        mainContainer.setMaxHeight(getAppHeight() * 0.8);
        mainContainer.setPadding(new Insets(20));

        Text title = new Text("SELECCIONA UN NIVEL");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: white;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(getAppHeight() * 0.6);

        VBox levelsContainer = new VBox(15);
        levelsContainer.setAlignment(Pos.TOP_CENTER);
        levelsContainer.setPadding(new Insets(10));

        List<LevelData> levels = createLevelList();

        final double levelWidth = getAppWidth() * 0.8;
        final double levelHeight = 150;

        for (LevelData level : levels) {
            HBox levelContainer = new HBox(15);
            levelContainer.setAlignment(Pos.CENTER_LEFT);
            levelContainer.setMinSize(levelWidth, levelHeight);
            levelContainer.setMaxSize(levelWidth, levelHeight);
            levelContainer.setStyle("-fx-background-color: rgba(40, 40, 45, 0.7); "
                    + "-fx-background-radius: 10; "
                    + "-fx-border-radius: 10; "
                    + "-fx-border-width: 2px; "
                    + "-fx-border-color: #555555;");

            ImageView levelImage = new ImageView();
            try {
                Image img = new Image(getClass().getResourceAsStream(level.imagePath));
                levelImage.setImage(img);
            } catch (Exception e) {
                levelImage.setImage(FXGL.image("default_level.png"));
            }
            levelImage.setFitWidth(200);
            levelImage.setFitHeight(120);
            levelImage.setPreserveRatio(true);

            VBox textContainer = new VBox(5);
            textContainer.setAlignment(Pos.CENTER_LEFT);
            textContainer.setPrefWidth(levelWidth - 230);
            textContainer.setMaxWidth(levelWidth - 230);

            Text nameLabel = new Text(level.name);
            nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: white;");
            nameLabel.setWrappingWidth(levelWidth - 240);

            Text descriptionLabel = new Text(level.description);
            descriptionLabel.setStyle("-fx-font-size: 12px; -fx-fill: #AAAAAA;");
            descriptionLabel.setWrappingWidth(levelWidth - 240);

            textContainer.getChildren().addAll(nameLabel, descriptionLabel);

            Button levelButton = new Button();
            levelButton.setGraphic(levelContainer);
            levelButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            levelButton.setMinSize(levelWidth, levelHeight);
            levelButton.setMaxSize(levelWidth, levelHeight);
            levelButton.setUserData(level.name);

            Tooltip tooltip = new Tooltip(level.description);
            tooltip.setShowDelay(Duration.seconds(0.3));
            tooltip.setStyle("-fx-font-size: 12px;");
            Tooltip.install(levelButton, tooltip);

            levelButton.setOnMouseEntered(e -> {
                levelContainer.setStyle("-fx-background-color: rgba(60, 60, 65, 0.7); "
                        + "-fx-background-radius: 10; "
                        + "-fx-border-radius: 10; "
                        + "-fx-border-width: 2px; "
                        + "-fx-border-color: #777777;");
            });

            levelButton.setOnMouseExited(e -> {
                if (level.name.equals(selectedLevel)) {
                    levelContainer.setStyle("-fx-background-color: rgba(50, 70, 50, 0.7); "
                            + "-fx-background-radius: 10; "
                            + "-fx-border-radius: 10; "
                            + "-fx-border-width: 2px; "
                            + "-fx-border-color: #4CAF50;");
                } else {
                    levelContainer.setStyle("-fx-background-color: rgba(40, 40, 45, 0.7); "
                            + "-fx-background-radius: 10; "
                            + "-fx-border-radius: 10; "
                            + "-fx-border-width: 2px; "
                            + "-fx-border-color: #555555;");
                }
            });

            levelButton.setOnAction(e -> selectLevel(level.name, levelContainer));

            levelContainer.getChildren().addAll(levelImage, textContainer);
            levelsContainer.getChildren().add(levelButton);
        }

        scrollPane.setContent(levelsContainer);

        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);

        Button backButton = createMenuButton("VOLVER", onBackToMainMenu);

        playButton = createMenuButton("JUGAR", () -> {
            if (selectedLevel != null) {
                onLevelSelected.accept(selectedLevel);
            }
        });
        playButton.setDisable(true);

        buttonContainer.getChildren().addAll(backButton, playButton);
        mainContainer.getChildren().addAll(title, scrollPane, buttonContainer);
        selectorPane.getChildren().add(mainContainer);
    }

    private void selectLevel(String levelName, HBox levelContainer) {
        selectorPane.lookupAll(".level-button").forEach(node -> {
            Button button = (Button) node;
            HBox container = (HBox) button.getGraphic();
            container.setStyle("-fx-background-color: rgba(40, 40, 45, 0.7); "
                    + "-fx-background-radius: 10; "
                    + "-fx-border-radius: 10; "
                    + "-fx-border-width: 2px; "
                    + "-fx-border-color: #555555;");
        });

        selectedLevel = levelName;
        levelContainer.setStyle("-fx-background-color: rgba(50, 70, 50, 0.7); "
                + "-fx-background-radius: 10; "
                + "-fx-border-radius: 10; "
                + "-fx-border-width: 2px; "
                + "-fx-border-color: #4CAF50;");

        playButton.setDisable(false);
    }

    private List<LevelData> createLevelList() {
        List<LevelData> levels = new ArrayList<>();
        levels.add(new LevelData("LA LUNA OLVIDADA", "/assets/textures/Menu/level1.jpg",
                "Un paisaje lunar con ruinas alienígenas y una puerta sellada que oculta el nucleo celestial, una luna llena de criaturas peligrosas."));
        levels.add(new LevelData("ESTACIÓN ESPACIAL", "/assets/textures/Menu/level2.jpg",
                "Un asteroide con pasillos flotantes estrechos, trampas, un portal desactivado y muchos secretos ocultos en sus caminos sin salida."));
        levels.add(new LevelData("CIUDAD FLOTANTE", "/assets/textures/Menu/level3.jpg",
                "Una estación espacial dentro de una anomalía gravitacional con peligros constantes y desafíos únicos."));
        return levels;
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setPrefSize(150, 40);
        button.setStyle("-fx-font-size: 14px; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);"
                + "-fx-text-fill: white; -fx-background-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 3, 0, 1, 1);");

        button.setOnMouseEntered(e -> button.setScaleX(1.05));
        button.setOnMouseExited(e -> button.setScaleX(1.0));

        button.setOnAction(e -> action.run());

        return button;
    }

    public StackPane getSelectorPane() {
        return selectorPane;
    }
}
