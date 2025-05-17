package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Luna extends Hero {
    public Luna() {
        super("Luna", 17, 6, 3, 2, 4, 2);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Instinto de Cazadora: revela enemigos ocultos y permite atacar.");
        resetCooldown();
    }
}