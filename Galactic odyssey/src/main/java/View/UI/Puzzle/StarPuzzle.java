package View.UI.Puzzle;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarPuzzle {
    private final Runnable onSuccess;
    private final int[] flashCounts;
    private final List<Circle> stars = new ArrayList<>();
    private final List<SequentialTransition> animations = new ArrayList<>();
    private StackPane root;

    public StarPuzzle(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        this.flashCounts = generateRandomFlashCounts();
    }

    public void show() {
        // Configuración del fondo
        root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        // Panel del puzzle
        VBox puzzleBox = new VBox(20);
        puzzleBox.setAlignment(Pos.CENTER);
        puzzleBox.setStyle("-fx-background-color: linear-gradient(to right, #1a1a2e, #16213e);"
                + "-fx-background-radius: 15;"
                + "-fx-padding: 30;"
                + "-fx-border-color: #2575fc;"
                + "-fx-border-radius: 15;"
                + "-fx-border-width: 3;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 20, 0, 0, 0);");

        // Título
        Label title = new Label("Código de las Estrellas");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        // Instrucciones
        Label instruction = new Label("Observa y cuenta cuántas veces parpadea cada estrella:");
        instruction.setFont(Font.font("Arial", 18));
        instruction.setTextFill(Color.LIGHTGRAY);

        // Contenedor de estrellas
        HBox starsContainer = new HBox(20);
        starsContainer.setAlignment(Pos.CENTER);

        // Crear estrellas (círculos) con sus animaciones
        for (int i = 0; i < 5; i++) {
            Circle star = new Circle(30);
            star.setFill(Color.TRANSPARENT);
            star.setStroke(Color.GOLD);
            star.setStrokeWidth(2);

            Label starLabel = new Label("Estrella " + (i + 1));
            starLabel.setTextFill(Color.WHITE);
            starLabel.setFont(Font.font(14));

            VBox starBox = new VBox(10, star, starLabel);
            starBox.setAlignment(Pos.CENTER);

            starsContainer.getChildren().add(starBox);
            stars.add(star);

            // Crear animación de parpadeo para esta estrella
            createStarAnimation(star, flashCounts[i]);
        }

        // Botón para iniciar animación
        Button startAnimationButton = createStyledButton("Iniciar Parpadeo", this::startAnimations);
        startAnimationButton.setStyle(startAnimationButton.getStyle() + "-fx-font-size: 16px;");

        // Campo de entrada
        TextField codeInput = new TextField();
        codeInput.setPromptText("Ingresa el código (suma total)");
        codeInput.setStyle("-fx-font-size: 16px; -fx-pref-width: 250px; -fx-pref-height: 40px;");
        codeInput.setMaxWidth(250);

        // Botón de enviar con la nueva lógica para cerrar el puzzle
        Button submitButton = createStyledButton("Validar Código", () -> {
            try {
                int userCode = Integer.parseInt(codeInput.getText());
                int correctCode = calculateCorrectCode();
                if (userCode == correctCode) {
                    stopAnimations();
                    FXGL.getNotificationService().pushNotification("Puede pasar al siguiente nivel"); // Mensaje al usuario
                    FXGL.getGameScene().removeUINode(root); // Cierra el puzzle
                    onSuccess.run();
                } else {
                    FXGL.getNotificationService().pushNotification("Código incorrecto. Intenta de nuevo.");
                }
            } catch (NumberFormatException e) {
                FXGL.getNotificationService().pushNotification("Ingresa un número válido.");
            }
        });

        // Ensamblar la UI
        puzzleBox.getChildren().addAll(
                title,
                instruction,
                starsContainer,
                startAnimationButton,
                codeInput,
                submitButton
        );
        root.getChildren().add(puzzleBox);

        // Agregar el puzzle a la escena de FXGL
        FXGL.getGameScene().addUINode(root);
    }

    private int[] generateRandomFlashCounts() {
        Random random = new Random();
        int[] counts = new int[5];
        for (int i = 0; i < 5; i++) {
            counts[i] = random.nextInt(5) + 1; // Cada estrella parpadea 1-5 veces
        }
        return counts;
    }

    private void createStarAnimation(Circle star, int flashCount) {
        SequentialTransition animation = new SequentialTransition();

        for (int i = 0; i < flashCount; i++) {
            // Animación de parpadeo (aparecer y desaparecer)
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), star);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.setDelay(Duration.millis(500 * i));

            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), star);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setDelay(Duration.millis(100));

            animation.getChildren().addAll(fadeIn, fadeOut);
        }

        // Al finalizar, dejar la estrella invisible
        animation.setOnFinished(e -> star.setFill(Color.TRANSPARENT));
        animations.add(animation);
    }

    private void startAnimations() {
        stopAnimations();
        for (SequentialTransition animation : animations) {
            animation.play();
        }
    }

    private void stopAnimations() {
        for (SequentialTransition animation : animations) {
            if (animation.getStatus() == Animation.Status.RUNNING) {
                animation.stop();
            }
        }
        // Resetear estrellas a transparente
        for (Circle star : stars) {
            star.setFill(Color.TRANSPARENT);
        }
    }

    private int calculateCorrectCode() {
        int sum = 0;
        for (int count : flashCounts) {
            sum += count;
        }
        return sum;
    }

    private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 18px; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);"
                + "-fx-text-fill: white; -fx-background-radius: 10px;");

        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 18px; -fx-background-color: linear-gradient(to right, #7a21db, #3585fc);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 18px; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);"));

        button.setOnMousePressed(e -> {
            ScaleTransition clickEffect = new ScaleTransition(Duration.millis(100), button);
            clickEffect.setToX(0.95);
            clickEffect.setToY(0.95);
            clickEffect.playFromStart();
        });

        button.setOnMouseReleased(e -> action.run());

        return button;
    }
}
