package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Titan extends Hero {
    public Titan() {
        super("Titan", 30, 3, 1, 2, 3, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Escudo de Energía: absorbe 5 puntos de daño para sí mismo o un aliado.");
        resetCooldown();
    }
}