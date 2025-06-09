package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class Nebula extends Hero {
    public Nebula() {
        super("Nebula", 15, 5, 0, 0, 0, 3);
    }

    @Override


    // Método para otorgar la pista y aumentar inteligencia
    public void useSpecialAbility() {
        grantWisdom();
        resetCooldown();
    }

    private void grantWisdom() {
        System.out.println("La Sabiduría Ancestral te otorga una pista y aumenta tu inteligencia.");
        revealHint(); // Muestra una pista
        increaseIntelligence(1); // Sube inteligencia en 1 punto
    }

    private void revealHint() {
        // Aquí defines cómo mostrar una pista (puede ser texto, visual, marcar enemigos, etc.)
        System.out.println("Pista: Algo se oculta cerca del bosque al norte.");
    }
}
