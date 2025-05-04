package Domain.Entity.enemies;

import Domain.Entity.Characters.Hero;
import Domain.Tile.Tile;
import java.util.List;

public class EnemyController {

    private Enemy enemy;
    private Hero hero; // Reference to the hero for AI

    public EnemyController(Enemy enemy, Hero hero) {
        this.enemy = enemy;
        this.hero = hero;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void update(List<Tile> obstacles) {
        enemy.updateMovement(hero, obstacles);
    }
}
