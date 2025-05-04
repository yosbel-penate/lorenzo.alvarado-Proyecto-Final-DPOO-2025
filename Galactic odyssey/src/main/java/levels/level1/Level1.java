package levels.level1;

import Domain.Entity.Characters.CaptainOrion;
import Domain.Entity.Characters.Hero;
import Domain.Entity.enemies.CombatDrone;
import Domain.Entity.enemies.Enemy;
import View.Maps.Map_1.MapLoader;
import com.almasb.fxgl.dsl.FXGL;
import levels.BaseLevel;
import java.util.ArrayList;
import java.util.List;

public class Level1 extends BaseLevel {

    private MapLoader mapLoader;
    private Hero hero;
    private List<Enemy> enemyList;

    @Override
    public void initLevel() {
        // Clear the game world and UI before loading the level
        FXGL.getGameWorld().removeEntities();
        FXGL.getGameScene().clearGameViews();
        FXGL.getGameScene().clearUINodes();

        // Load the map for level 1
        mapLoader = new MapLoader();
        mapLoader.loadMap();

        // Create and spawn the hero
        hero = new CaptainOrion();
        hero.spawn();
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        hero.startTurn(heroes);

        // Initialize and spawn enemies (for example, 5 drones)
        enemyList = new ArrayList<>();
        enemyList.add(new CombatDrone());
        enemyList.add(new CombatDrone());
        enemyList.add(new CombatDrone());
        enemyList.add(new CombatDrone());
        enemyList.add(new CombatDrone());

        for (Enemy enemy : enemyList) {
            enemy.spawn(hero);
        }
    }

    // Methods to access the hero and the enemy list if needed
    public Hero getHero() {
        return hero;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public MapLoader getMapLoader() {
        return mapLoader;
    }
}
