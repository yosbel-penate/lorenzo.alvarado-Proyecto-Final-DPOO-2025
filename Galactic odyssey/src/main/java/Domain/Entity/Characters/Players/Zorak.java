package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.Characters.Enemies.EnemyComponent;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;

import java.util.Random;

class Zorak extends Hero {
    public Zorak() {
        super("Zorak", 24, 4, 1, 4, 6, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println(getName() + " usa Golpe de Energía: daña a todos los enemigos en un radio de 2 casillas.");

        int radius = 2;

        // Obtener las entidades de tipo ENEMY
        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemyEntity -> {
            EnemyComponent ec = enemyEntity.getComponent(EnemyComponent.class);
            Enemy enemy = ec.getEnemy();

            int enemyX = enemy.getX();
            int enemyY = enemy.getY();

            // Comparar la distancia entre el héroe y el enemigo
            if (getDistanceTo(enemyX, enemyY) <= radius) {
                // Otra forma, usando un nuevo objeto Random:
                int damage = new Random().nextInt(maxDamage - minDamage + 1) + minDamage;
                enemy.takeDamage(damage);
                System.out.println(enemy.getName() + " recibió " + damage + " de daño.");
            }
        });

        resetCooldown();
    }


}