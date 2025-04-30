package App;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class App extends GameApplication {
    private MenuController menuController;
    private MapLoader mapLoader;
    private Player player;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1500);
        settings.setHeight(800);
        settings.setTitle("Galactic Odyssey");
    }

    @Override
    protected void initGame() {
        // Registrar fábrica
        getGameWorld().addEntityFactory(new GameFactory());
    }
    protected void initUI() {
        // Una vez que la UI está lista, mostramos el menú
        menuController = new MenuController();
        menuController.showMenu();
    }

    public void startNewGame() {
        // Limpiar mundo y UI
        getGameWorld().removeEntities();
        FXGL.getGameScene().clearGameViews();
        FXGL.getGameScene().clearUINodes();

        // Cargar mapa y jugador
        mapLoader = new MapLoader();
        mapLoader.loadMap();

        player = new Player();
        player.spawnPlayer();
        player.initInput();
    }


    public static void main(String[] args) {
        launch(args);
    }
}