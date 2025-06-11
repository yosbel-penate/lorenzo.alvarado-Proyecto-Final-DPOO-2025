package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.Characters.Enemies.EnemyComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;

class Blaze extends Hero {
    public Blaze() {
        super("Blaze", 19, 5, 4, 3, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Disparo Explosivo: causa 5-8 puntos de daño en un radio de 2 casillas.");
        resetCooldown();
    }

    // Nuevo método: Ataque Explosivo que daña a enemigos dentro de un radio de 2 casillas
    public void explosiveAttack(Entity target, int radius) {
        int distance = getDistanceTo((int)target.getX(), (int)target.getY());
        if (distance <= radius) {
            // Causa daño entre 5 y 8
            int damage = FXGL.random(5, 8);
            System.out.println("Blaze ataca a " + target.getType() + " con Disparo Explosivo, causando " + damage + " puntos de daño.");

            // Verificamos si la entidad tiene el componente EnemyComponent
            if (target.hasComponent(EnemyComponent.class)) {
                EnemyComponent enemyComponent = target.getComponent(EnemyComponent.class);  // Obtener el componente
                enemyComponent.takeDamage(damage);  // Aplicamos el daño
            } else {
                System.out.println("El objetivo no es un enemigo.");
            }

        } else {
            System.out.println("El enemigo está fuera de alcance para el Disparo Explosivo.");
        }
    }

    // Método que se puede usar para obtener la distancia
    public int getDistanceTo(int tx, int ty) {
        return Math.abs(tx - getX()) + Math.abs(ty - getY());
    }
}
