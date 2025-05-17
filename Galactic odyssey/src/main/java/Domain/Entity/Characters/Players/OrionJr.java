package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class OrionJr extends Hero {
    public OrionJr() {
        super("Orion Jr.", 18, 5, 2, 1, 3, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Potencial Desbloqueado: duplica daño y defensa por 1 turno.");
        resetCooldown();
    }
}