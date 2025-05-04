package levels.level2;

import View.Maps.Map_1.MapLoader; // Or the corresponding package for the level 2 map, if different
import Domain.Entity.Characters.CaptainOrion;
import Domain.Entity.Characters.Hero;
import com.almasb.fxgl.dsl.FXGL;
import Domain.Entity.enemies.Enemy;
import Domain.Entity.enemies.CombatDrone;
import java.util.ArrayList;
import java.util.List;

public class Level2 {

    private MapLoader mapLoader;
    private Hero hero;
    private List<Enemy> enemyList;

    public void initLevel() {
        // Clear the game world and UI before loading the level
        FXGL.getGameWorld().removeEntities();
        FXGL.getGameScene().clearGameViews();
        FXGL.getGameScene().clearUINodes();

        // Create and assign the MapLoader for Level2.
        // You can use the same MapLoader class or one with different configurations for this level.
        mapLoader = new MapLoader();
        mapLoader.loadMap();

        // Create and spawn the hero
        hero = new CaptainOrion(); // You can use the same hero or create a different type
        hero.spawn();
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        hero.startTurn(heroes);

        // Initialize and spawn the enemies for Level2.
        enemyList = new ArrayList<>();
        enemyList.add(new CombatDrone());
        enemyList.add(new CombatDrone());
        enemyList.add(new CombatDrone());
        // You can add other enemy types or more instances as required.

        for (Enemy enemy : enemyList) {
            enemy.spawn(hero);
        }
    }

    public Hero getHero() {
        return hero;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    // Expose the MapLoader so that the App class can obtain obstacles.
    public MapLoader getMapLoader() {
        return mapLoader;
    }
}
