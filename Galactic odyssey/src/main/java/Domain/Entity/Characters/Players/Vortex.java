package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Vortex extends Hero {
    public Vortex() {
        super("Vortex", 18, 5, 5, 3, 6, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Tormenta Cósmica: daña a todos los enemigos en un radio de 4 casillas.");
        resetCooldown();
    }
}