package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.Characters.Enemies.EnemyComponent;
import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

class Luna extends Hero {
    public Luna() {
        super("Luna", 17, 6, 3, 2, 4, 2);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Instinto de Cazadora: revela enemigos ocultos y permite atacar.");
        revealHiddenEnemies();  // Llamamos al método para revelar enemigos ocultos
        resetCooldown();
    }

    // Método para revelar enemigos ocultos
    private void revealHiddenEnemies() {
        int revealRange = 5;
        int centerX = getX();
        int centerY = getY();

        // Obtener todas las entidades tipo ENEMY
        List<Entity> enemyEntities = FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY);

        for (Entity entity : enemyEntities) {
            EnemyComponent enemyComponent = entity.getComponent(EnemyComponent.class);
            if (enemyComponent != null) {
                Enemy enemy = enemyComponent.getEnemy();

                if (enemy.isHidden()) {
                    int enemyX = enemy.getX();
                    int enemyY = enemy.getY();

                    int distance = Math.abs(centerX - enemyX) + Math.abs(centerY - enemyY);

                    if (distance <= revealRange) {
                        enemy.setHidden(false);
                        System.out.println("Enemigo revelado: " + enemy.getName() +
                                " en la posición (" + enemyX + ", " + enemyY + ")");
                    }
                }
            }
        }

        System.out.println("Todos los enemigos ocultos dentro de un radio de " + revealRange + " casillas han sido revelados.");
    }

}
