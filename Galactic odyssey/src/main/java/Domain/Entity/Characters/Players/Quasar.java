package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Quasar extends Hero {
    public Quasar() {
        super("Quasar", 17, 5, 3, 1, 2, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Reparación Rápida: restaura 2-4 puntos de armadura.");
        resetCooldown();
    }
}
