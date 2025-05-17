package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Eclipse extends Hero {
    public Eclipse() {
        super("Eclipse", 15, 7, 2, 3, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Ataque Furtivo: inflige 6-8 puntos si el enemigo está desprevenido.");
        resetCooldown();
    }
}