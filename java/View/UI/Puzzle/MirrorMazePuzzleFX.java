package View.UI.Puzzle;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MirrorMazePuzzleFX {
    private final Runnable onSuccess;
    private final String[] correctPattern = {"izquierda", "derecha", "centro"};
    private final String[] playerPattern = new String[3];
    private int step = 0;
    private StackPane root;
    private List<Rectangle> mirrorShapes = new ArrayList<>();

    public MirrorMazePuzzleFX(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void show() {
        root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        VBox puzzleBox = new VBox(20);
        puzzleBox.setAlignment(Pos.CENTER);
        puzzleBox.setStyle("-fx-background-color: linear-gradient(to right, #1a1a2e, #16213e);"
                + "-fx-background-radius: 15;"
                + "-fx-padding: 30;"
                + "-fx-border-color: #2575fc;"
                + "-fx-border-radius: 15;"
                + "-fx-border-width: 3;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 20, 0, 0, 0);");

        Label title = new Label("Laberinto de los Espejos");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        Label instruction = new Label("Coloca los espejos en la dirección correcta.");
        instruction.setFont(Font.font("Arial", 18));
        instruction.setTextFill(Color.LIGHTGRAY);

        HBox mirrorsContainer = new HBox(20);
        mirrorsContainer.setAlignment(Pos.CENTER);

        for (int i = 0; i < 3; i++) {
            Rectangle mirror = new Rectangle(50, 100);
            mirror.setFill(Color.GOLD);
            mirror.setStroke(Color.WHITE);
            mirror.setStrokeWidth(3);
            mirrorShapes.add(mirror);
            mirrorsContainer.getChildren().add(mirror);
        }

        ComboBox<String> directionBox = new ComboBox<>();
        directionBox.getItems().addAll("izquierda", "derecha", "centro");
        directionBox.setStyle("-fx-font-size: 16px; -fx-pref-width: 250px;");

        Button submitButton = createStyledButton("Colocar Espejo", () -> placeMirror(directionBox));

        Label resultLabel = new Label();
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultLabel.setTextFill(Color.WHITE);

        puzzleBox.getChildren().addAll(title, instruction, mirrorsContainer, directionBox, submitButton, resultLabel);
        root.getChildren().add(puzzleBox);

        FXGL.getGameScene().addUINode(root);
    }
    private void placeMirror(ComboBox<String> directionBox) {
        String selectedDirection = directionBox.getValue();

        if (selectedDirection == null) {
            FXGL.getNotificationService().pushNotification("Selecciona una dirección antes de continuar.");
            return;
        }

        if (step < 3) {
            playerPattern[step] = selectedDirection;

            if (selectedDirection.equals(correctPattern[step])) {
                mirrorShapes.get(step).setFill(Color.BLUE);
            } else {
                mirrorShapes.get(step).setFill(Color.RED);
            }

            mirrorShapes.get(step).setRotate(getRotationForDirection(selectedDirection));
            step++;
        }

        if (step == 3) {
            checkSolution();
        }
    }

    private double getRotationForDirection(String direction) {
        switch (direction) {
            case "izquierda":
                return -30;
            case "derecha":
                return 30;
            default:
                return 0;
        }
    }

    private void checkSolution() {
        boolean correct = true;
        for (int i = 0; i < 3; i++) {
            if (!playerPattern[i].equals(correctPattern[i])) {
                correct = false;
                break;
            }
        }

        if (correct) {
            FXGL.getNotificationService().pushNotification("¡Correcto! Puede pasar al siguiente nivel.");
            playGlowEffect();
            FXGL.getGameScene().removeUINode(root);
            onSuccess.run();
        } else {
            step = 0;
            FXGL.getNotificationService().pushNotification("Patrón incorrecto. Inténtalo de nuevo.");
            resetMirrors();
        }
    }

    private void playGlowEffect() {
        for (Rectangle mirror : mirrorShapes) {
            FadeTransition glowEffect = new FadeTransition(Duration.seconds(1), mirror);
            glowEffect.setFromValue(1.0);
            glowEffect.setToValue(0.3);
            glowEffect.setCycleCount(2);
            glowEffect.setAutoReverse(true);
            glowEffect.play();
        }
    }

    private void resetMirrors() {
        for (Rectangle mirror : mirrorShapes) {
            mirror.setFill(Color.GOLD);
            mirror.setRotate(0);
        }
    }

    private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 18px; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);"
                + "-fx-text-fill: white; -fx-background-radius: 10px;");

        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 18px; -fx-background-color: linear-gradient(to right, #7a21db, #3585fc);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 18px; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);"));

        button.setOnMousePressed(e -> action.run());

        return button;
    }
}