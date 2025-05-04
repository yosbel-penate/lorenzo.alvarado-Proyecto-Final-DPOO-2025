package App;

import Domain.Entity.Characters.CaptainOrion;
import Domain.Entity.Characters.Hero;
import Domain.Entity.EntityType;
import Domain.Entity.enemies.CombatDrone;
import Domain.Tile.Tile; // Antes: Domain.casilla.Casilla -> ahora Tile
import Domain.Entity.*;
import Domain.MagicObjet.*;
import View.Maps.GameFactory;
import View.Maps.Map_1.MapLoader;
import View.UI.MenuController;
import Domain.Entity.enemies.EnemyController;
import javafx.scene.input.MouseButton;
import javafx.application.Platform;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import Domain.Entity.enemies.Enemy; // La clase Enemy, que antes se llamaba Enemies
import javafx.scene.input.KeyCode;
import com.almasb.fxgl.input.UserAction;
import javafx.util.Duration;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;

// Importar las clases de nivel
import levels.level1.Level1;
import levels.level2.Level2;

public class MainApp extends GameApplication {

    // Controlador del menú
    private MenuController menuController;
    // Cargador del mapa
    private MapLoader mapLoader;
    // Instancia del héroe
    private Hero hero;
    // Lista global de enemigos para el nivel actual
    private List<Enemy> enemyList = new ArrayList<>();

    // Indica si es el turno del jugador
    private boolean isPlayerTurn = true;

    // Contador para saber el nivel actual (inicia en 1)
    private int currentLevelIndex = 1;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1500);
        settings.setHeight(800);
        settings.setTitle("Galactic Odyssey");
    }

    @Override
    protected void initGame() {
        // Registrar enemigos y la factoría de entidades
        Enemy.registerEnemy("enemy", data -> new CombatDrone());
        FXGL.getGameWorld().addEntityFactory(new GameFactory());
        // Iniciar el juego cargando el primer nivel
        startNewGame();
    }

    @Override
    protected void initUI() {
        // Mostrar el menú de inicio
        menuController = new MenuController();
        menuController.showMenu();
    }

    @Override
    protected void initInput() {
        // Acción para mover al héroe a la izquierda
        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                if (isPlayerTurn && hero != null) {
                    hero.moveLeft();
                }
            }
        }, KeyCode.A);

        // Acción para mover al héroe a la derecha
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                if (isPlayerTurn && hero != null) {
                    hero.moveRight();
                }
            }
        }, KeyCode.D);

        // Acción para mover al héroe hacia arriba
        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onActionBegin() {
                if (isPlayerTurn && hero != null) {
                    hero.moveUp();
                }
            }
        }, KeyCode.W);

        // Acción para mover al héroe hacia abajo
        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                if (isPlayerTurn && hero != null) {
                    hero.moveDown();
                }
            }
        }, KeyCode.S);

        // Acción para terminar el turno del héroe (ENTER)
        FXGL.getInput().addAction(new UserAction("End Turn") {
            @Override
            protected void onActionBegin() {
                if (isPlayerTurn) {
                    endTurn();
                }
            }
        }, KeyCode.ENTER);

        // Acción para el ataque del héroe (clic izquierdo del ratón)
        FXGL.getInput().addAction(new UserAction("Hero Attack") {
            @Override
            protected void onActionBegin() {
                Point2D mousePos = FXGL.getInput().getMousePositionWorld();
                List<Entity> enemyEntities = FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY);

                for (Entity enemyEntity : enemyEntities) {
                    var bbc = enemyEntity.getBoundingBoxComponent();
                    double minX = enemyEntity.getX();
                    double minY = enemyEntity.getY();
                    double width = bbc.getWidth();
                    double height = bbc.getHeight();
                    Rectangle2D enemyRect = new Rectangle2D(minX, minY, width, height);

                    if (enemyRect.contains(mousePos.getX(), mousePos.getY())) {
                        // Recuperar el controlador y la instancia del enemigo
                        EnemyController controller = enemyEntity.getProperties().getObject("controller");
                        Enemy enemy = controller.getEnemy();

                        int dx = Math.abs(hero.getPositionHero().getX() - enemy.getPositionEnemies().getX());
                        int dy = Math.abs(hero.getPositionHero().getY() - enemy.getPositionEnemies().getY());
                        int manhattanDistance = dx + dy;

                        int attackRange = (hero instanceof CaptainOrion) ? 3 : 4;

                        if (manhattanDistance <= attackRange) {
                            int damage = ThreadLocalRandom.current().nextInt(2, 5);
                            enemy.takeDamage(damage);
                            System.out.println(hero.getName() + " attacks " + enemy.getSpritePath()
                                    + " inflicting " + damage + " damage (distance: " + manhattanDistance + ").");

                            // Si el enemigo es derrotado (salud <= 0)...
                            if (enemy.getHealth() <= 0) {
                                enemy.removeFromWorld(); // Elimina la entidad FXGL del mundo del juego
                                enemyList.remove(enemy);
                                System.out.println("Enemy defeated!");
                            }
                        } else {
                            System.out.println("Enemy is out of range (distance: " + manhattanDistance
                                    + ", range: " + attackRange + ").");
                        }
                        // Ataca solamente a un enemigo por clic
                        break;
                    }
                }
                // Verificar si se ha cumplido la condición de victoria después del ataque
                checkVictoryCondition();
            }
        }, MouseButton.PRIMARY);

        // Acción para activar la habilidad especial (clic derecho del ratón)
        FXGL.getInput().addAction(new UserAction("Special Ability") {
            @Override
            protected void onActionBegin() {
                List<Hero> allies = new ArrayList<>();
                hero.startTurn(allies);
                System.out.println(hero.getName() + " activates its special ability.");
            }
        }, MouseButton.SECONDARY);
    }

    // Método para terminar el turno del héroe y permitir que los enemigos actúen
    public void endTurn() {
        isPlayerTurn = false;
        List<Tile> obstacles = mapLoader.getObstacles(); // Obtener los obstáculos del mapa
        FXGL.runOnce(() -> {
            for (Enemy enemy : enemyList) {
                enemy.enemyTurn(obstacles);
            }
            isPlayerTurn = true;
            checkVictoryCondition();
        }, Duration.seconds(1));
    }

    // Método para verificar si se han derrotado todos los enemigos
    private void checkVictoryCondition() {
        FXGL.runOnce(() -> {
            if (enemyList.isEmpty()) {
                System.out.println("All enemies defeated! Proceeding to the next level...");
                nextLevel();
            }
        }, Duration.seconds(0.5));
    }

    // Inicia un nuevo juego cargando el primer nivel
    public void startNewGame() {
        currentLevelIndex = 1;
        loadLevel(currentLevelIndex);
    }

    // Carga el nivel correspondiente al índice dado
    private void loadLevel(int levelNum) {
        // Limpiar el mundo del juego y la UI antes de cargar un nuevo nivel
        FXGL.getGameWorld().removeEntities(); // Elimina todas las entidades del mundo del juego
        // Limpia todas las entidades del juego
        FXGL.getGameScene().clearGameViews();
        FXGL.getGameScene().clearUINodes();

        if (levelNum == 1) {
            Level1 level = new Level1();
            level.initLevel();
            hero = level.getHero();
            enemyList = level.getEnemyList();
            mapLoader = level.getMapLoader();
        } else if (levelNum == 2) {
            Level2 level = new Level2();
            level.initLevel();
            hero = level.getHero();
            enemyList = level.getEnemyList();
            mapLoader = level.getMapLoader();
        } else {
            System.out.println("You have completed the game!");
            Platform.runLater(() -> FXGL.getGameController().exit());
        }
    }

    // Avanza al siguiente nivel aumentando el índice y cargándolo
    public void nextLevel() {
        currentLevelIndex++;
        loadLevel(currentLevelIndex);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
