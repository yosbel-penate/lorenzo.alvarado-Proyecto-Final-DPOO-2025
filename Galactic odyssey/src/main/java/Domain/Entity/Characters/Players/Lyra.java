package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Lyra extends Hero {
    public Lyra() {
        super("Lyra", 18, 5, 4, 0, 0, 2);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Curación Cósmica: restaura 3-5 puntos de salud a aliados en un radio de 4 casillas.");
        resetCooldown();
    }
}