package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Aurora extends Hero {
    public Aurora() {
        super("Aurora", 14, 6, 0, 0, 0, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Negociación: evita combates con facciones enemigas en un radio de 6 casillas.");
        resetCooldown();
    }
}
