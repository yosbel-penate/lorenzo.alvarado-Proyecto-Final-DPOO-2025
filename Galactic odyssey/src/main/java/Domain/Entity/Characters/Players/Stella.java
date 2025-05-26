package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Stella extends Hero {
    public Stella() {
        super("Stella", 16, 5, 6, 2, 4, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Control Mental: controla a un enemigo débil durante 1 turno.");
        resetCooldown();
    }
}