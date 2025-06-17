package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.Characters.Enemies.EnemyComponent;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

import static Domain.Entity.EntityType.ENEMY;

public class Comet extends Hero {

    public Comet() {
        super("Comet", 14, 9, 2, 1, 2, 2, "comet.png");
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Golpe Relámpago: ataca dos veces (2-4 daño por golpe).");

        List<Entity> enemies = FXGL.getGameWorld().getEntitiesByType(ENEMY);

        for (Entity enemyEntity : enemies) {
            Enemy enemy = enemyEntity.getComponent(EnemyComponent.class).getEnemy();
            int distance = getDistanceTo(enemy.getX(), enemy.getY());

            if (distance <= getAttackRange()) {
                int totalDamage = getRandomDamage(2, 4) + getRandomDamage(2, 4);
                enemy.takeDamage(totalDamage);
                System.out.println("Golpe Relámpago inflige " + totalDamage + " a " + enemy.name);
                break; // Solo ataca a uno
            }
        }

        resetCooldown();
    }

    private int getRandomDamage(int min, int max) {
        return FXGLMath.random( 2, 5);

    }
}
