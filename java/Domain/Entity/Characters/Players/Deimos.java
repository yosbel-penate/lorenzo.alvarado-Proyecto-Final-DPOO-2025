package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Deimos extends Hero {
    private boolean isHidden = false; // Para saber si está oculto

    public Deimos() {
        super("Deimos", 17, 6, 3, 2, 4, 4, "deimos.png");
    }

    @Override
    public void useSpecialAbility() {
        if (isHidden) {
            // Si ya está oculto, se desencadena el ataque
            System.out.println("Deimos inflige 6-8 de daño tras haber estado oculto.");
            attack();  // Aquí llamamos al método de ataque para infligir daño.
            isHidden = false; // Deja de estar oculto
        } else {
            // Si no está oculto, lo ocultamos por 2 turnos
            System.out.println("Deimos se oculta por 2 turnos.");
            isHidden = true;
        }

        resetCooldown();
    }

    // Método para realizar el daño de ataque
    private void attack() {
        // Cálculo de daño aleatorio entre 6 y 8
        int damage = (int) (Math.random() * (8 - 6 + 1)) + 6;
        System.out.println("Deimos inflige " + damage + " puntos de daño.");
    }
}
