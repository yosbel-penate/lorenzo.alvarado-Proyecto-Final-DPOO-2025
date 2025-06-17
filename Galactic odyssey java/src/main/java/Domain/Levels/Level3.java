package Domain.Levels;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Level3 {

    private StackPane rootPane;
    private Runnable onBack;

    public Level3(Runnable onBack) {
        this.onBack = onBack;
    }

    public void initialize(List<String> selectedCharacters) {
        rootPane = new StackPane();

        var background = texture("fondo_menu.png");
        background.setFitWidth(getAppWidth());
        background.setFitHeight(getAppHeight());

        VBox content = new VBox(20);
        content.setTranslateY(getAppHeight() / 3);
        content.setAlignment(javafx.geometry.Pos.CENTER);

        Label label = new Label("Próximamente");
        label.setFont(new Font(64));
        label.setStyle("-fx-text-fill: white;");

        Button btnRegresar = new Button("Regresar");
        btnRegresar.setPrefWidth(150);
        btnRegresar.setOnAction(e -> {
            // Quitar esta UI
            getGameScene().removeUINode(rootPane);
            // Llamar callback para volver al menú
            if (onBack != null) {
                onBack.run();
            }
        });

        content.getChildren().addAll(label, btnRegresar);

        rootPane.getChildren().addAll(background, content);

        getGameScene().addUINode(rootPane);
    }
}
