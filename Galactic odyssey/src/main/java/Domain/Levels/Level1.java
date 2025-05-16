package Domain.Levels;

import Domain.Entity.Characters.Enemies.EnemyEntity;
import Domain.Entity.Characters.Players.PlayerEntity;
import Domain.Entity.Combat.TurnManager;
import View.Maps.MapLoader;
import View.UI.PlayerHUD;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class Level1 {
    private MapLoader mapLoader;
    private List<PlayerEntity> playerEntities;
    private TurnManager turnManager;
    private PlayerHUD playerHUD;
    private List<EnemyEntity> enemies = new ArrayList<>();

    public void initialize(List<String> selectedCharacters) {
        FXGL.getGameWorld().removeEntities();
        FXGL.getGameScene().clearGameViews();
        FXGL.getGameScene().clearUINodes();

        mapLoader = new MapLoader();
        mapLoader.loadMap("Map1.tmx");

        turnManager = new TurnManager();
        playerEntities = new ArrayList<>();
        int startX = 5;
        int startY = 15;

        for (int i = 0; i < selectedCharacters.size(); i++) {
            int offsetX = i % 3 - 1;
            int offsetY = i / 3;

            PlayerEntity playerEntity = new PlayerEntity(
                    selectedCharacters.get(i),
                    startX + offsetX,
                    startY + offsetY
            );

            playerEntity.setTurnManager(turnManager);
            playerEntity.spawnPlayer();
            playerEntities.add(playerEntity);
            turnManager.addPlayer(playerEntity);
        }

        if (!playerEntities.isEmpty()) {
            turnManager.addPlayer(playerEntities.get(0));
            playerEntities.get(0).startTurn();
        }

        for (int i = 0; i < playerEntities.size(); i++) {
            PlayerEntity player = playerEntities.get(i);
            PlayerHUD hud = new PlayerHUD(player, i);
            turnManager.addPlayer(player);
            turnManager.addHUD(hud); // NUEVO
        }


        FXGL.getGameScene().getRoot().getStylesheets().add("assets/ui/hud-styles.css");

        List<Point2D> enemyPositions = new ArrayList<>();
        enemyPositions.add(new Point2D(15, 15));
        enemyPositions.add(new Point2D(15, 13));
        enemyPositions.add(new Point2D(14, 15));

        spawnEnemies(enemyPositions);
    }

    private void spawnEnemies(List<Point2D> enemyPositions) {
        enemies.clear();
        for (Point2D position : enemyPositions) {
            EnemyEntity enemy = new EnemyEntity(playerEntities, turnManager);
            enemy.spawnEnemy((int) position.getX(), (int) position.getY());
            enemies.add(enemy);
            turnManager.addEnemy(enemy);
        }
    }

    public void damageEnemy(EnemyEntity enemy, int damage) {
        enemy.getLogic().takeDamage(damage);
        enemy.showDamage(damage);
    }

    public PlayerHUD getPlayerHUD() {
        return playerHUD;
    }

    public List<PlayerEntity> getPlayerEntities() {
        return playerEntities;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }
}