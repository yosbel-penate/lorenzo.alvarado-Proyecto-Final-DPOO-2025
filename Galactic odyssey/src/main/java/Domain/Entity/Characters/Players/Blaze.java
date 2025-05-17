package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Blaze extends Hero {
    public Blaze() {
        super("Blaze", 19, 5, 4, 3, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Disparo Explosivo: causa 5-8 puntos de daño en un radio de 2 casillas.");
        resetCooldown();
    }
}