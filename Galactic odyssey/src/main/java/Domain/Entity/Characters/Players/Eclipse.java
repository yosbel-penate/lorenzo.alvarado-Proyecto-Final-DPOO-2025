package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Eclipse extends Hero {
    public Eclipse() {
        super("Eclipse", 15, 7, 2, 3, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Ataque Furtivo: inflige 6-8 puntos si el enemigo está desprevenido.");
        performSneakAttack();  // Llamamos a un método para realizar el ataque furtivo
        resetCooldown();
    }

    // Método para realizar el daño del ataque furtivo
    private void performSneakAttack() {
        // Lógica de daño aleatorio entre 6 y 8
        int damage = (int) (Math.random() * (8 - 6 + 1)) + 6;
        System.out.println("Eclipse inflige " + damage + " puntos de daño con un ataque furtivo.");
    }
}
