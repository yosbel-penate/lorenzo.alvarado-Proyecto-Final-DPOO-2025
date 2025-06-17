package View.UI.Menu;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CharacterSelectionController {

    private StackPane selectionPane;
    private List<String> selectedCharacters = new ArrayList<>();
    private Button playButton;
    private Consumer<List<String>> onSelectionComplete;
    private Runnable onBackToMenu;
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

    public CharacterSelectionController(Consumer<List<String>> onSelectionComplete, Runnable onBackToMenu) {
        this.onSelectionComplete = onSelectionComplete;
        this.onBackToMenu = onBackToMenu;
        selectionPane = new StackPane();

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

        playButton = createMenuButton("JUGAR", this::startGame);
        playButton.setDisable(true);

        Button backButton = createMenuButton("VOLVER", onBackToMenu);

        buttonPanel.getChildren().addAll(backButton, playButton);
        mainContainer.getChildren().addAll(title, selectionCounter, scrollPane, buttonPanel);
        selectionPane.getChildren().add(mainContainer);
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
        characters.add(new CharacterData("Andrómeda", "/assets/textures/Menu/andromeda.png", 20, "Ritual Estelar: cura aliados o daña enemigos (5-7 curación o 4-6 daño)."));
        characters.add(new CharacterData("Aurora", "/assets/textures/Menu/aurora.png", 14, "Negociación: evita combates con facciones enemigas en un radio de 6 casillas."));
        characters.add(new CharacterData("Blaze", "/assets/textures/Menu/blaze.png", 19, "Disparo Explosivo: causa 5-8 puntos de daño en un radio de 2 casillas."));
        characters.add(new CharacterData("Capitán Orion", "/assets/textures/Menu/cap_orion.png", 20, "Inspiración Galáctica: aumenta ataque y defensa de aliados cercanos por 2 turnos."));
        characters.add(new CharacterData("Comet", "/assets/textures/Menu/comet.png", 14, "Golpe Relámpago: ataca dos veces (2-4 daño por golpe)."));
        characters.add(new CharacterData("Cygnus", "/assets/textures/Menu/cygnus.png", 16, "Maniobra Evasiva: esquiva y aumenta velocidad."));
        characters.add(new CharacterData("Deimos", "/assets/textures/Menu/deimos.png", 17, "Infiltración: se oculta por 2 turnos y luego inflige 6-8."));
        characters.add(new CharacterData("Eclipse", "/assets/textures/Menu/eclipse.png", 15, "Ataque Furtivo: daña a un enemigo desprevenido por 6-8 puntos."));
        characters.add(new CharacterData("Lyra", "/assets/textures/Menu/lyra.png", 18, "Curación Cósmica: restaura 3-5 puntos de salud a aliados cercanos."));
        characters.add(new CharacterData("Luna", "/assets/textures/Menu/luna.png", 17, "Instinto de Cazadora: revela enemigos ocultos y permite ataque inmediato."));
        characters.add(new CharacterData("Nebula", "/assets/textures/Menu/nebula.png", 15, "Sabiduría Ancestral: da pistas y aumenta inteligencia de aliados."));
        characters.add(new CharacterData("Nova", "/assets/textures/Menu/nova.png", 16, "Desactivación de Trampas: desactiva trampas en un radio de 3 casillas."));
        characters.add(new CharacterData("Orion Jr.", "/assets/textures/Menu/orion_jr.png", 18, "Potencial Desbloqueado: duplica daño y defensa por 1 turno."));
        characters.add(new CharacterData("Phobos", "/assets/textures/Menu/phobos.png", 22, "Disparo Letal: causa 7-9 daño a enemigos heridos y aturde."));
        characters.add(new CharacterData("Quasar", "/assets/textures/Menu/quasar.png", 17, "Reparación Rápida: repara equipos y restaura 2-4 puntos de armadura."));
        characters.add(new CharacterData("Solara", "/assets/textures/Menu/solara.png", 25, "Muralla de Luz: bloquea todo el daño a aliados cercanos por 1 turno."));
        characters.add(new CharacterData("Stella", "/assets/textures/Menu/stella.png", 16, "Control Mental: controla a un enemigo débil durante 1 turno."));
        characters.add(new CharacterData("Titan", "/assets/textures/Menu/titan.png", 30, "Escudo de Energía: absorbe 5 puntos de daño para sí mismo o un aliado."));
        characters.add(new CharacterData("Vortex", "/assets/textures/Menu/vortex.png", 18, "Tormenta Cósmica: daña a todos los enemigos en un radio de 4 casillas."));
        characters.add(new CharacterData("Zorak", "/assets/textures/Menu/zorak.png", 24, "Golpe de Energía: daña a todos los enemigos en un radio de 2 casillas."));
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

    private void startGame() {
        if (selectedCharacters.size() == 5) {
            onSelectionComplete.accept(selectedCharacters);
        }
    }

    public StackPane getSelectionPane() {
        return selectionPane;
    }
}