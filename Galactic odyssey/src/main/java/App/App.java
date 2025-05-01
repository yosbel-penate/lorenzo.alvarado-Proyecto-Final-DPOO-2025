package App;

import View.Maps.GameFactory;
import View.Maps.Map_1.MapLoader;
import View.UI.MenuController;
import Domain.Entity.Characters.Players.Player;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

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
        // Registrar el GameFactory para spawnear entidades (obstáculos, etc.)
        getGameWorld().addEntityFactory(new GameFactory());
    }

    @Override
    protected void initUI() {
        menuController = new MenuController();
        menuController.showMenu();
    }

    public void startNewGame() {
        // Se limpia el mundo y la UI
        getGameWorld().removeEntities();
        FXGL.getGameScene().clearGameViews();
        FXGL.getGameScene().clearUINodes();

        mapLoader = new MapLoader();
        mapLoader.loadMap();
        // Se spawnea únicamente al jugador.
        player = new Player();
        player.spawnPlayer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
