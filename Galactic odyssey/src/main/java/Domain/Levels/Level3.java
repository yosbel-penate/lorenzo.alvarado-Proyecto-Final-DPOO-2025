package Domain.Levels;

import Domain.Entity.Characters.Enemies.EnemyEntity;
import Domain.Entity.Characters.Players.PlayerEntity;
import Domain.Entity.Combat.TurnManager;
import View.Maps.MapLoader;
import View.UI.PlayerHUD;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Level3 {
    private MapLoader mapLoader;
    private List<PlayerEntity> playerEntities;
    private TurnManager turnManager;
    private List<EnemyEntity> enemies = new ArrayList<>();

    public void initialize(List<String> selectedCharacters) {
        FXGL.getGameWorld().removeEntities();
        FXGL.getGameScene().clearGameViews();
        FXGL.getGameScene().clearUINodes();

        mapLoader = new MapLoader();
        mapLoader.loadMap("Map3.tmx"); // Usa tu mapa correspondiente

        turnManager = new TurnManager();
        playerEntities = new ArrayList<>();
        int startX = 3;
        int startY = 12;

        for (int i = 0; i < selectedCharacters.size(); i++) {
            int offsetX = i % 3 - 1;
            int offsetY = i / 3;

            PlayerEntity player = new PlayerEntity(selectedCharacters.get(i), startX + offsetX, startY + offsetY);
            player.setTurnManager(turnManager);
            player.spawnPlayer();
            playerEntities.add(player);
            turnManager.addPlayer(player);
        }

        if (!playerEntities.isEmpty()) {
            playerEntities.get(0).startTurn();
        }

        for (int i = 0; i < playerEntities.size(); i++) {
            PlayerHUD hud = new PlayerHUD(playerEntities.get(i), i);
            turnManager.addHUD(hud);
        }

        FXGL.getGameScene().getRoot().getStylesheets().add("assets/ui/hud-styles.css");

        List<Point2D> enemyPositions = List.of(
                new Point2D(16, 14),
                new Point2D(17, 12),
                new Point2D(18, 14)
        );

        spawnEnemies(enemyPositions);
    }

    private void spawnEnemies(List<Point2D> enemyPositions) {
        enemies.clear();
        for (Point2D pos : enemyPositions) {
            EnemyEntity enemy = new EnemyEntity(playerEntities, turnManager);
            enemy.spawnEnemy((int) pos.getX(), (int) pos.getY());
            enemies.add(enemy);
            turnManager.addEnemy(enemy);
        }
    }
}
