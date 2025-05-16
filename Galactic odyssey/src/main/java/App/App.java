package App;

import Domain.Levels.Level1;
import View.Maps.GameFactory;
import View.UI.Menu.MenuController;
import View.UI.Menu.MyLoadingScene;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

public class App extends GameApplication {
    private MenuController menuController;
    private Level1 level1;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setSceneFactory(new SceneFactory() {
            public LoadingScene newLoadingScene() {
                return new MyLoadingScene();
            }
        });
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
        settings.setHeight(1080);
        settings.setHeight(617);
        settings.setTitle("Galactic Odyssey");
        settings.setVersion("0.0.1");
        settings.setManualResizeEnabled(false);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameFactory());
    }

    @Override
    protected void initUI() {
        menuController = new MenuController(this::startNewGame);
        menuController.showMenu();
    }

    public void startNewGame(List<String> selectedCharacters) {
        level1 = new Level1();
        level1.initialize(selectedCharacters);
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
        if (level1 != null && level1.getPlayerHUD() != null) {
            level1.getPlayerHUD().updateHUD();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
