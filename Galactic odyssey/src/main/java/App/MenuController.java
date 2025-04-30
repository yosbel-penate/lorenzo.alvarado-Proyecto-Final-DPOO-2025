package App;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import static com.almasb.fxgl.dsl.FXGL.*;

public class MenuController {

    public void showMenu() {
        showBackgroundImage();
        showMainMenu();
    }

    private void showBackgroundImage() {
        Texture backgroundImage = FXGL.texture("fondo_menu.png");
        backgroundImage.setFitWidth(getAppWidth());
        backgroundImage.setFitHeight(getAppHeight());

        getGameScene().addUINode(backgroundImage);
    }

    private void showMainMenu() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Galactic Odyssey");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startButton = new Button("Iniciar Juego");
        Button exitButton = new Button("Salir");

        startButton.setPrefSize(250, 60);
        exitButton.setPrefSize(250, 60);

        startButton.setStyle("-fx-font-size: 18px;");
        exitButton.setStyle("-fx-font-size: 18px;");

        startButton.setOnAction(e -> {
            ((App) FXGL.getApp()).startNewGame();
        });
        exitButton.setOnAction(e -> getGameController().exit());

        menu.getChildren().addAll(title, startButton, exitButton);
        menu.setTranslateX(50);
        menu.setTranslateY(100);

        addUINode(menu);
    }
}