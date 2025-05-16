package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Comet extends Hero {
    public Comet() {
        super("Comet", 14, 9, 2, 1, 2, 2);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Golpe Relámpago: ataca dos veces (2-4 daño por golpe).");
        resetCooldown();
    }
}