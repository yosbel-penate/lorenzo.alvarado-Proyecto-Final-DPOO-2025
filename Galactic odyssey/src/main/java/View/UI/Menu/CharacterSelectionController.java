package App;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionController {

    private StackPane selectionPane;
    private List<String> selectedCharacters = new ArrayList<>();
    private Button playButton;
    private Runnable onSelectionComplete;
    private Label selectionCounter;

    private static class CharacterData {
        String name;
        String imagePath;
        int health;
        String description;

        public CharacterData(String name, String imagePath, int health, String description) {
            this.name = name;
            this.imagePath = imagePath;
            this.health = health;
            this.description = description;
        }
    }

    public CharacterSelectionController(Runnable onSelectionComplete) {
        this.onSelectionComplete = onSelectionComplete;
        selectionPane = new StackPane();

        // Fondo semitransparente
        Rectangle bg = new Rectangle(800, 600, Color.rgb(0, 0, 0, 0.85));
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        selectionPane.getChildren().add(bg);

        showCharacterSelection();
    }

    private void showCharacterSelection() {
        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setMaxSize(780, 580);

        Label title = new Label("SELECCIONA 5 PERSONAJES");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        selectionCounter = new Label("0/5 seleccionados");
        selectionCounter.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(450);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setStyle("-fx-background-color: transparent;");

        List<CharacterData> characters = createCharacterList();

        final double cellWidth = 100;
        final double cellHeight = 130;

        for (int i = 0; i < characters.size(); i++) {
            CharacterData character = characters.get(i);

            ImageView characterImage = new ImageView();
            try {
                Image img = new Image(character.imagePath);
                characterImage.setImage(img);
            } catch (Exception e) {
                characterImage.setImage(FXGL.image("default_character.png"));
            }
            characterImage.setFitWidth(70);
            characterImage.setFitHeight(70);
            characterImage.setPreserveRatio(true);

            Label nameLabel = new Label(character.name);
            nameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: white;");
            nameLabel.setMaxWidth(cellWidth - 15);
            nameLabel.setWrapText(true);

            Label healthLabel = new Label("Vida: " + character.health);
            healthLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #AAAAAA;");

            VBox characterBox = new VBox(5);
            characterBox.setAlignment(Pos.CENTER);
            characterBox.setMinSize(cellWidth, cellHeight);
            characterBox.setMaxSize(cellWidth, cellHeight);
            characterBox.getChildren().addAll(characterImage, nameLabel, healthLabel);

            Button characterButton = new Button();
            characterButton.setGraphic(characterBox);
            characterButton.setStyle("-fx-background-color: rgba(40, 40, 45, 0.9); "
                    + "-fx-background-radius: 8; "
                    + "-fx-border-radius: 8; "
                    + "-fx-border-width: 1px; "
                    + "-fx-border-color: #555555; "
                    + "-fx-padding: 0; "
                    + "-fx-cursor: hand;");
            characterButton.setMinSize(cellWidth, cellHeight);
            characterButton.setMaxSize(cellWidth, cellHeight);
            characterButton.setUserData(character.name);

            Tooltip tooltip = new Tooltip(character.description);
            tooltip.setShowDelay(Duration.seconds(0.3));
            tooltip.setStyle("-fx-font-size: 12px;");
            Tooltip.install(characterButton, tooltip);

            characterButton.setOnMouseEntered(e -> {
                characterButton.setStyle("-fx-background-color: rgba(60, 60, 65, 0.9); "
                        + "-fx-background-radius: 8; "
                        + "-fx-border-radius: 8; "
                        + "-fx-border-width: 1px; "
                        + "-fx-border-color: #777777; "
                        + "-fx-padding: 0; "
                        + "-fx-cursor: hand;");
            });

            characterButton.setOnMouseExited(e -> {
                if (selectedCharacters.contains(character.name)) {
                    characterButton.setStyle("-fx-background-color: rgba(50, 70, 50, 0.9); "
                            + "-fx-background-radius: 8; "
                            + "-fx-border-radius: 8; "
                            + "-fx-border-width: 2px; "
                            + "-fx-border-color: #4CAF50; "
                            + "-fx-padding: 0; "
                            + "-fx-cursor: hand;");
                } else {
                    characterButton.setStyle("-fx-background-color: rgba(40, 40, 45, 0.9); "
                            + "-fx-background-radius: 8; "
                            + "-fx-border-radius: 8; "
                            + "-fx-border-width: 1px; "
                            + "-fx-border-color: #555555; "
                            + "-fx-padding: 0; "
                            + "-fx-cursor: hand;");
                }
            });

            characterButton.setOnAction(e -> {
                toggleCharacterSelection(character.name, characterButton);
                updateSelectionCounter();
            });

            int row = i / 4;
            int col = i % 4;
            grid.add(characterButton, col, row);
        }

        scrollPane.setContent(grid);
        scrollPane.setVvalue(0);

        HBox buttonPanel = new HBox(20);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setStyle("-fx-padding: 15 0 15 0;");

        playButton = createMenuButton("JUGAR", this::showLevelSelector);
        playButton.setDisable(true);

        Button backButton = createMenuButton("VOLVER", this::returnToMenu);

        buttonPanel.getChildren().addAll(backButton, playButton);
        mainContainer.getChildren().addAll(title, selectionCounter, scrollPane, buttonPanel);
        selectionPane.getChildren().add(mainContainer);
    }

    private void showLevelSelector() {
        if (selectedCharacters.size() == 5) {
            LevelSelectorController levelSelector = new LevelSelectorController(this::returnToMenu);
            selectionPane.getChildren().clear();

            Rectangle bg = new Rectangle(800, 600, Color.rgb(0, 0, 0, 0.85));
            bg.setArcWidth(20);
            bg.setArcHeight(20);

            selectionPane.getChildren().addAll(bg, levelSelector.getSelectorPane());
        }
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setPrefSize(160, 40);
        button.setStyle("-fx-font-size: 14px; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);"
                + "-fx-text-fill: white; -fx-background-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 3, 0, 1, 1);");

        button.setOnMouseEntered(e -> button.setScaleX(1.05));
        button.setOnMouseExited(e -> button.setScaleX(1.0));

        button.setOnAction(e -> action.run());

        return button;
    }

    private List<CharacterData> createCharacterList() {
        List<CharacterData> characters = new ArrayList<>();
        characters.add(new CharacterData("Guerrero", "warrior.png", 150, "Fuerte y resistente"));
        characters.add(new CharacterData("Mago", "wizard.png", 80, "Poder mágico devastador"));
        characters.add(new CharacterData("Arquero", "archer.png", 100, "Precisión a distancia"));
        characters.add(new CharacterData("Asesino", "assassin.png", 90, "Ataques rápidos y letales"));
        characters.add(new CharacterData("Sanador", "healer.png", 70, "Cura a sus aliados"));
        characters.add(new CharacterData("Tanque", "tank.png", 200, "Alta resistencia"));
        characters.add(new CharacterData("Nigromante", "necromancer.png", 85, "Invoca no muertos"));
        characters.add(new CharacterData("Berserker", "berserker.png", 120, "Furia en batalla"));
        characters.add(new CharacterData("Paladín", "paladin.png", 130, "Protección divina"));
        characters.add(new CharacterData("Ladrón", "thief.png", 95, "Sigilo y astucia"));
        characters.add(new CharacterData("Druida", "druid.png", 90, "Control de la naturaleza"));
        characters.add(new CharacterData("Mercenario", "mercenary.png", 110, "Versátil en combate"));
        characters.add(new CharacterData("Cazador", "hunter.png", 105, "Rastreo y trampas"));
        characters.add(new CharacterData("Caballero", "knight.png", 140, "Armadura pesada"));
        characters.add(new CharacterData("Brujo", "warlock.png", 85, "Magia oscura"));
        characters.add(new CharacterData("Monje", "monk.png", 100, "Artes marciales"));
        characters.add(new CharacterData("Pícaro", "rogue.png", 95, "Ataques sorpresa"));
        characters.add(new CharacterData("Elementalista", "elementalist.png", 75, "Control elemental"));
        characters.add(new CharacterData("Clérigo", "cleric.png", 80, "Sanación y protección"));
        characters.add(new CharacterData("Explorador", "ranger.png", 110, "Supervivencia y arquería"));

        return characters;
    }

    private void toggleCharacterSelection(String characterName, Button button) {
        if (selectedCharacters.contains(characterName)) {
            selectedCharacters.remove(characterName);
            button.setStyle("-fx-background-color: rgba(40, 40, 45, 0.9); "
                    + "-fx-background-radius: 8; "
                    + "-fx-border-radius: 8; "
                    + "-fx-border-width: 1px; "
                    + "-fx-border-color: #555555; "
                    + "-fx-padding: 0; "
                    + "-fx-cursor: hand;");
        } else if (selectedCharacters.size() < 5) {
            selectedCharacters.add(characterName);
            button.setStyle("-fx-background-color: rgba(50, 70, 50, 0.9); "
                    + "-fx-background-radius: 8; "
                    + "-fx-border-radius: 8; "
                    + "-fx-border-width: 2px; "
                    + "-fx-border-color: #4CAF50; "
                    + "-fx-padding: 0; "
                    + "-fx-cursor: hand;");
        }

        playButton.setDisable(selectedCharacters.size() != 5);
        updateSelectionCounter();
    }

    private void updateSelectionCounter() {
        selectionCounter.setText("Seleccionados: " + selectedCharacters.size() + "/5");
        selectionCounter.setStyle("-fx-font-size: 16px; -fx-text-fill: " +
                (selectedCharacters.size() == 5 ? "#4CAF50" : "white") + ";");
    }

    private void returnToMenu() {
        onSelectionComplete.run();
    }

    public StackPane getSelectionPane() {
        return selectionPane;
    }
}