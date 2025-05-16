package Domain.Entity.Combat;

import Domain.Entity.Characters.Enemies.DroneCombate;
import Domain.Entity.Characters.Players.CapOrion;

public class CombatSystem {
    private CapOrion player;
    private DroneCombate enemy;

    public CombatSystem(CapOrion player, DroneCombate enemy) {
        this.player = player;
        this.enemy = enemy;
        player.setPosition(0, 0);
        enemy.setPosition(10, 10);
    }

    public void simulateTurn() {
        if (!player.isAlive() || !enemy.isAlive()) return;

        int distance = player.getDistanceTo(enemy.getX(), enemy.getY());
        if (distance <= player.attackRange) {
            int damage = random(player.minDamage, player.maxDamage);
            enemy.health -= damage;
            System.out.println(player.name + " ataca al enemigo y causa " + damage + " de daño. Vida del enemigo: " + enemy.health);
        } else {
            System.out.println(player.name + " se mueve hacia el enemigo.");
            for (int i = 0; i < player.moveRange; i++) player.moveToward(enemy.getX(), enemy.getY());
        }

        if (!enemy.isAlive()) return;

        distance = enemy.getDistanceTo(player.getX(), player.getY());
        if (distance <= enemy.attackRange) {
            int damage = random(enemy.minDamage, enemy.maxDamage);
            player.health -= damage;
            System.out.println(enemy.name + " ataca al jugador y causa " + damage + " de daño. Vida del jugador: " + player.health);
        } else if (distance <= 10) {
            System.out.println(enemy.name + " se mueve hacia el jugador.");
            for (int i = 0; i < enemy.moveRange; i++) enemy.moveToward(player.getX(), player.getY());
        } else {
            System.out.println(enemy.name + " espera (fuera de rango)." );
        }
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public boolean isBattleOngoing() {
        return player.isAlive() && enemy.isAlive();
    }
}
