package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Deimos extends Hero {
    public Deimos() {
        super("Deimos", 17, 6, 3, 2, 4, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Infiltración: se oculta por 2 turnos y luego inflige 6-8.");
        resetCooldown();
    }
}