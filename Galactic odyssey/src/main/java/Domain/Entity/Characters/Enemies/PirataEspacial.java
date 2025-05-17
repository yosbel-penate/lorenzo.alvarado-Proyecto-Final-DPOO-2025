package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Enemies.Enemy;

class PirataEspacial extends Enemy {
    public PirataEspacial() {
        super("Pirata Espacial", 20, 5, 4, 2, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Robo: roba un objeto aleatorio de un personaje.");
        resetCooldown();
    }
}