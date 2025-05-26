package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Phobos extends Hero {
    public Phobos() {
        super("Phobos", 22, 6, 5, 3, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Disparo Letal: inflige 7-9 si el enemigo está bajo 50% de vida y lo aturde.");
        resetCooldown();
    }
}