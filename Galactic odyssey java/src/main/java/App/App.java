package App;

import Domain.Entity.EntityType;
import Domain.Levels.Level1;
import View.Maps.GameFactory;
import View.UI.Menu.MenuController;
import View.UI.Menu.MyLoadingScene;
import View.UI.Menu.WinScreen;
import View.UI.Puzzle.StarPuzzle;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.input.KeyCode; // Import correcto
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.stage.Stage;


import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class App extends GameApplication {
    private MenuController menuController;
    private Level1 level1;
    private boolean riddleShown = false;
    private WinScreen winScreen;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public LoadingScene newLoadingScene() {
                return new MyLoadingScene();
            }
        });
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
        settings.setHeight(1080);
        settings.setWidth(1080);
        settings.setTitle("Galactic Odyssey");
        settings.setVersion("0.0.1");
        settings.setManualResizeEnabled(false);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameFactory());
        initPhysics();

        // Atajo F1 para mostrar el puzzle
        onKeyDown(KeyCode.P, "Mostrar Puzzle", () -> {
            System.out.println("¡F1 presionada!"); // Depuración
            if (!riddleShown) {
                showStarPuzzle();
                riddleShown = true; // Corrección para evitar mostrar múltiples veces
            }
        });

        List<Entity> obstacles = getGameWorld().getEntitiesByType(EntityType.OBSTACULOS);
        System.out.println("Número de obstáculos en el mapa: " + obstacles.size());
        obstacles.forEach(o -> System.out.println("Obstáculo en X: " + o.getX() + ", Y: " + o.getY()));
    }

    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.HERO, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity hero, Entity enemy) {
                PhysicsComponent physics = hero.getComponent(PhysicsComponent.class);
                Point2D velocity = physics.getLinearVelocity();
                Vec2 velocityVec = new Vec2((float) velocity.getX(), (float) velocity.getY());
                hero.getComponent(PhysicsComponent.class).overwritePosition(hero.getPosition().subtract(velocityVec.toPoint2D()));
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.HERO, EntityType.OBSTACULOS) {
            @Override
            protected void onCollisionBegin(Entity hero, Entity obstacle) {
                PhysicsComponent physics = hero.getComponent(PhysicsComponent.class);
                Point2D velocity = physics.getLinearVelocity();
                hero.getComponent(PhysicsComponent.class).overwritePosition(hero.getPosition().subtract(velocity));
            }
        });
    }

    @Override
    protected void initUI() {
        menuController = new MenuController(this::startNewGame);
        menuController.showMenu();
    }

    public void startNewGame(List<String> selectedCharacters) {
        level1 = new Level1();
        level1.initialize(selectedCharacters);
        riddleShown = false;
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
        if (level1 != null && level1.getPlayerHUD() != null) {
            level1.getPlayerHUD().updateHUD();
        }

        if (level1 != null && !riddleShown) {
            checkEnemiesDefeated();
        }
    }

    private void checkEnemiesDefeated() {
        if (getGameWorld().getEntitiesByType(EntityType.ENEMY).isEmpty()) {
            showStarPuzzle();
            riddleShown = true;
        }
    }

    private void showStarPuzzle() {
        System.out.println("Mostrando StarPuzzle..."); // Depuración
        new StarPuzzle(() -> {
            getNotificationService().pushNotification("¡Código correcto! Avanzando...");
            Stage stage = getPrimaryStage();

            if (winScreen == null) {
                winScreen = new WinScreen(stage);
            }
            winScreen.show();
        }).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
