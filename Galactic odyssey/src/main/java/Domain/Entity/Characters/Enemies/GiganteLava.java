package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Enemies.Enemy;

class GiganteLava extends Enemy {
    public GiganteLava() {
        super("Gigante de Lava", 28, 2, 1, 5, 7, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Erupción: daña a todos los personajes en un radio de 3 casillas (4 daño).");
        resetCooldown();
    }
}