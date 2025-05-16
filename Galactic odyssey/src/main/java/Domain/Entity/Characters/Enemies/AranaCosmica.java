package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Enemies.Enemy;

class AranaCosmica extends Enemy {
    public AranaCosmica() {
        super("Araña Cósmica", 18, 3, 2, 3, 4, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Red de Energía: atrapa a un personaje en un radio de 3 casillas.");
        resetCooldown();
    }
}
