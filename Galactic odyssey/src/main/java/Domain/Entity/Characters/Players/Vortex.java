package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.Characters.Enemies.EnemyComponent;
import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;

import java.util.Random;

class Vortex extends Hero {
    public Vortex() {
        super("Vortex", 18, 5, 5, 3, 6, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Tormenta Cósmica: daña a todos los enemigos en un radio de 4 casillas.");

        int vortexX = getX();
        int vortexY = getY();
        Random random = new Random();

        FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(e -> {
            Enemy enemy = e.getComponent(EnemyComponent.class).getEnemy();
            int distance = Math.abs(enemy.getX() - vortexX) + Math.abs(enemy.getY() - vortexY);
            if (distance <= 4) {
                int damage = random.nextInt(maxDamage - minDamage + 1) + minDamage;
                enemy.takeDamage(damage);
                System.out.println(enemy.getName() + " recibió " + damage + " de daño de la Tormenta Cósmica.");
            }
        });

        resetCooldown();
    }
}