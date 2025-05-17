package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Nebula extends Hero {
    public Nebula() {
        super("Nebula", 15, 5, 0, 0, 0, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Sabiduría Ancestral: da una pista y aumenta inteligencia.");
        resetCooldown();
    }
}