package Domain.Entity.Combat;

import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.Characters.Players.Hero;

public class CombatSystem {
    private Hero player;
    private Enemy enemy;

    public CombatSystem(Hero player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        player.setPosition(0, 0);
        enemy.setPosition(10, 10);
    }

    public void simulateTurn() {
        if (!player.isAlive() || !enemy.isAlive()) return;

        // Turno del jugador
        int distance = player.getDistanceTo(enemy.getX(), enemy.getY());
        if (distance <= player.getAttackRange()) {
            int damage = random(player.minDamage, player.maxDamage);
            enemy.takeDamage(damage);
            System.out.println(player.getName() + " ataca al enemigo y causa " + damage + " de daño. Vida del enemigo: " + enemy.getHealth());
        } else {
            System.out.println(player.getName() + " se mueve hacia el enemigo.");
            for (int i = 0; i < player.getMoveRange(); i++) {
                player.moveToward(enemy.getX(), enemy.getY());
            }
        }

        if (!enemy.isAlive()) return;

        // Turno del enemigo
        distance = enemy.getDistanceTo(player.getX(), player.getY());
        if (distance <= enemy.getAttackRange()) {
            int damage = random(enemy.minDamage, enemy.maxDamage);
            player.takeDamage(damage);
            System.out.println(enemy.getName() + " ataca al jugador y causa " + damage + " de daño. Vida del jugador: " + player.health);
        } else if (distance <= 10) {
            System.out.println(enemy.getName() + " se mueve hacia el jugador.");
            for (int i = 0; i < enemy.moveRange; i++) {
                enemy.moveToward(player.getX(), player.getY());
            }
        } else {
            System.out.println(enemy.getName() + " espera (fuera de rango).");
        }
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public boolean isBattleOngoing() {
        return player.isAlive() && enemy.isAlive();
    }
}
