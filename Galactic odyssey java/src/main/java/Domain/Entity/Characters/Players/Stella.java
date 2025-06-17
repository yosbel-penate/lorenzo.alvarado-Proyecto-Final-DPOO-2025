package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.Enemy;

import java.util.List;

class Stella extends Hero {
    private List<Enemy> enemies; // Lista de enemigos disponibles (esto depende de cómo los tengas en tu juego)

    public Stella() {
        super("Stella", 16, 5, 6, 2, 4, 3,"stella.png");
        }

    @Override
    public void useSpecialAbility() {
        System.out.println("Control Mental: controla a un enemigo débil durante 1 turno.");

        // Buscar al enemigo más débil
        Enemy targetEnemy = findWeakestEnemy();

        if (targetEnemy != null) {
            // Realizar control mental sobre el enemigo más débil
            System.out.println(targetEnemy.getName() + " ha sido controlado.");
            targetEnemy.setControlled(true); // Suponiendo que Enemy tiene un método setControlled
        } else {
            System.out.println("No hay enemigos débiles para controlar.");
        }

        resetCooldown();
    }

    // Método para encontrar al enemigo más débil (como te lo expliqué antes)
    public Enemy findWeakestEnemy() {
        if (enemies == null || enemies.isEmpty()) {
            return null; // Si no hay enemigos, retorna null
        }

        Enemy weakestEnemy = null;
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {  // Solo considerar enemigos vivos
                if (weakestEnemy == null || enemy.getHealth() < weakestEnemy.getHealth()) {
                    weakestEnemy = enemy;
                }
            }
        }

        return weakestEnemy; // Retorna el enemigo más débil
    }
}
