package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Cygnus extends Hero {
    public Cygnus() {
        super("Cygnus", 16, 7, 3, 2, 3, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Maniobra Evasiva: esquiva y aumenta velocidad.");
        resetCooldown();
    }
}