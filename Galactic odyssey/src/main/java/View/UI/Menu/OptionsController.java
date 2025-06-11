package View.UI.Menu;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class OptionsController {

    private StackPane optionsPane;
    private Runnable onBackToMenu;

    public OptionsController(Runnable onBackToMenu) {
        this.onBackToMenu = onBackToMenu;
        this.optionsPane = new StackPane();
        initializeUI();
    }

    private void initializeUI() {
        // Fondo semitransparente
        Rectangle bg = new Rectangle(600, 400, Color.rgb(0, 0, 0, 0.8));
        bg.setArcWidth(20);
        bg.setArcHeight(20);

        VBox optionsMenu = new VBox(20);
        optionsMenu.setAlignment(Pos.CENTER);
        optionsMenu.setTranslateY(-20);

        Label title = new Label("OPCIONES");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Control de volumen de música
        Label musicLabel = new Label("Volumen Música:");
        musicLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Slider musicSlider = new Slider(0, 100, FXGL.getSettings().getGlobalMusicVolume() * 100);
        musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            FXGL.getSettings().setGlobalMusicVolume(newVal.doubleValue() / 100);
        });

        // Control de volumen de efectos
        Label sfxLabel = new Label("Volumen Efectos:");
        sfxLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Slider sfxSlider = new Slider(0, 100, FXGL.getSettings().getGlobalSoundVolume() * 100);
        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            FXGL.getSettings().setGlobalSoundVolume(newVal.doubleValue() / 100);
        });

        // Botón de regreso
        Button backButton = new Button("Volver");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #2575fc; -fx-text-fill: white;");
        backButton.setOnAction(e -> onBackToMenu.run());

        optionsMenu.getChildren().addAll(
                title,
                musicLabel, musicSlider,
                sfxLabel, sfxSlider,
                backButton
        );

        optionsPane.getChildren().addAll(bg, optionsMenu);
    }

    public StackPane getOptionsPane() {
        return optionsPane;
    }
}