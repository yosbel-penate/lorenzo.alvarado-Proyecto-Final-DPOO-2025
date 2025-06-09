package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.EnemyComponent;
import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

class Phobos extends Hero {
    public Phobos() {
        super("Phobos", 22, 6, 5, 3, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Disparo Letal: inflige 7-9 si el enemigo está bajo 50% de vida y lo aturde.");

        // Verifica si el enemigo está bajo el 50% de vida
        if (enemyIsUnder50PercentHealth()) {
            int damage = getRandomDamage();
            System.out.println("Disparo Letal inflige: " + damage + " de daño.");

            // Aturdir al enemigo
            stunEnemy();

            resetCooldown();
        } else {
            System.out.println("El enemigo no está bajo el 50% de vida, habilidad no activada.");
        }
    }

    // Verifica si el enemigo está bajo el 50% de vida
    private boolean enemyIsUnder50PercentHealth() {
        // Asumimos que tienes acceso al enemigo a través de una variable
        // Esto debería adaptarse a cómo gestionas a los enemigos en tu juego
        Enemy targetEnemy = getTargetEnemy(); // Método que obtiene al enemigo objetivo
        return targetEnemy.getHealth() < (targetEnemy.getMaxHealth() / 2);
    }

    // Obtiene un valor aleatorio de daño entre 7 y 9
    private int getRandomDamage() {
        return (int) (Math.random() * (9 - 7 + 1)) + 7;
    }

    // Aturdir al enemigo (esto podría ser más complejo dependiendo de cómo manejas el estado de aturdimiento)
    private void stunEnemy() {
        Enemy targetEnemy = getTargetEnemy(); // Obtén el enemigo objetivo
        targetEnemy.stun(); // Asumimos que tienes un método `stun()` en la clase Enemy
        System.out.println("El enemigo ha sido aturdido.");
    }
    private Enemy getTargetEnemy() {
        // Obtener todas las entidades de tipo ENEMY (de tipo com.almasb.fxgl.entity.Entity)
        List<Entity> allEntities = FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY);

        // Supongo que el héroe tiene un método para obtener su posición en el mapa
        int heroX = this.getX();
        int heroY = this.getY();

        // Variable para almacenar al enemigo más cercano
        Enemy targetEnemy = null;
        int minDistance = Integer.MAX_VALUE; // Usamos la distancia más corta para encontrar el enemigo más cercano

        // Recorremos todas las entidades
        for (com.almasb.fxgl.entity.Entity entity : allEntities) {
            // Verificamos si la entidad tiene el componente EnemyComponent
            EnemyComponent enemyComponent = entity.getComponent(EnemyComponent.class);

            // Si la entidad tiene el componente EnemyComponent, es un enemigo
            if (enemyComponent != null) {
                // Obtenemos el enemigo real desde el EnemyComponent
                Enemy enemy = enemyComponent.getEnemy();

                // Verificamos si el enemigo es realmente válido
                if (enemy != null) {
                    // Obtener la posición del enemigo
                    int enemyX = enemy.getX();
                    int enemyY = enemy.getY();

                    // Calculamos la distancia Manhattan entre el héroe y el enemigo
                    int distance = Math.abs(heroX - enemyX) + Math.abs(heroY - enemyY);

                    // Si encontramos un enemigo más cercano, lo asignamos como objetivo
                    if (distance < minDistance) {
                        minDistance = distance;
                        targetEnemy = enemy;
                    }
                }
            }
        }

        return targetEnemy; // Devuelve el enemigo más cercano
    }
    // Este es solo un ejemplo. Deberías definir cómo obtienes al enemigo objetivo en tu juego.

}
