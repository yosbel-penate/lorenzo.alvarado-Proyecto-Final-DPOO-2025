package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Enemies.Enemy;

class DevoradorEstelar extends Enemy {
    public DevoradorEstelar() {
        super("Devorador Estelar", 100, 4, 6, 6, 8, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Colapso Gravitacional: afecta a todos los personajes en un radio de 5 casillas (4-6 daño y los inmoviliza).");
        resetCooldown();
    }

    public void usarFase1() {
        System.out.println("Llamarada Cósmica: 3-5 puntos de daño a todos los personajes en radio de 4 casillas.");
    }

    public void usarFase2() {
        System.out.println("Agujero Negro: 5-7 daño y atrapa a personajes en una casilla aleatoria.");
    }

    public void usarFase3() {
        System.out.println("Explosión Final: causa 10 puntos de daño y puede terminar la partida si no es derrotado a tiempo.");
    }
}