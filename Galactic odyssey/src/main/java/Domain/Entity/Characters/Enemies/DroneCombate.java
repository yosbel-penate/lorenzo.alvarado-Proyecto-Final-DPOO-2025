package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Enemies.Enemy;

public class DroneCombate extends Enemy {
    public DroneCombate() {
        super("Drone de Combate", 12, 4, 3, 2, 3, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Escaneo: detecta personajes ocultos en un radio de 5 casillas.");
        resetCooldown();
    }
}
