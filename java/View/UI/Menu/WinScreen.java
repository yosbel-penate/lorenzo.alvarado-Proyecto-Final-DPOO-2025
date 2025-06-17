package View.UI.Menu;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WinScreen {

    private Stage stage;

    public WinScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        double width = stage.getWidth();
        double height = stage.getHeight();

        Image backgroundImage = new Image("assets/textures/fondo_menu.png");
        ImageView background = new ImageView(backgroundImage);

        background.setFitWidth(width);
        background.setFitHeight(height);

        Text winText = new Text("WIN");
        winText.setFont(Font.font(60));
        winText.setStyle("-fx-fill: white;");

        Button btnSalir = new Button("Salir");
        Button btnMenu = new Button("Volver al menú");

        btnSalir.setOnAction(e -> FXGL.getGameController().exit());
        btnMenu.setOnAction(e -> FXGL.getGameController().startNewGame());

        animateButtonColor(btnSalir);
        animateButtonColor(btnMenu);

        VBox vbox = new VBox(20, winText, btnMenu, btnSalir);
        vbox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(background, vbox);

        Scene scene = new Scene(root, width, height);

        stage.setScene(scene);
        stage.setFullScreen(true);  // Mantener pantalla completa
        stage.show();
    }

    private void animateButtonColor(Button button) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(button.styleProperty(), "-fx-background-color: #0000FF; -fx-text-fill: white;")),  // azul
                new KeyFrame(Duration.seconds(1), new KeyValue(button.styleProperty(), "-fx-background-color: #800080; -fx-text-fill: white;")), // morado
                new KeyFrame(Duration.seconds(2), new KeyValue(button.styleProperty(), "-fx-background-color: #0000FF; -fx-text-fill: white;"))  // vuelve a azul
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
