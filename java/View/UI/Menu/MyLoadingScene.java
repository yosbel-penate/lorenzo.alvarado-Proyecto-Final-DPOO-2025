package View.UI.Menu;

import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MyLoadingScene extends LoadingScene {

    public MyLoadingScene() {
        super();

        // Configuración del diseño principal
        StackPane root = new StackPane();
        root.setPrefSize(FXGL.getAppWidth(), FXGL.getAppHeight());
        root.setStyle("-fx-background-color: black;"); // Fondo negro

        // Agregar etiqueta "Cargando..."
        Label loadingLabel = new Label("Cargando...");
        loadingLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        // Agregar título opcional del juego
        Text titleText = FXGL.getUIFactoryService().newText("Galactic Odyssey", 32);
        titleText.setStyle("-fx-fill: white;");

        root.getChildren().addAll(titleText, loadingLabel);
        StackPane.setAlignment(titleText, Pos.TOP_CENTER);
        StackPane.setAlignment(loadingLabel, Pos.CENTER);

        // Agregar todo al contenido principal
        getContentRoot().getChildren().add(root);
    }

    @Override
    public void onUpdate(double tpf) {
        // Usar onUpdate para ejecutar el temporizador después de la inicialización completa
        FXGL.getGameTimer().runOnceAfter(() -> {
            System.out.println("La pantalla de carga ha terminado.");
            // Aquí puedes agregar lógica para cambiar a la siguiente escena
        }, Duration.seconds(5));
    }
}