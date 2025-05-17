package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Andromeda extends Hero {
    public Andromeda() {
        super("Andrómeda", 20, 5, 6, 3, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Ritual Estelar: cura aliados o daña enemigos (5-7 curación o 4-6 daño).");
        resetCooldown();
    }
}