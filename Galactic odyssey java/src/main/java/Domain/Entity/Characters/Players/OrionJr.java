package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;

class OrionJr extends Hero {
    public OrionJr() {
        super("Orion Jr.", 18, 5, 2, 1, 3, 4,"orionJr.png");
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Potencial Desbloqueado: duplica daño y defensa por 1 turno.");
        activatePowerUp();
        resetCooldown();
    }

    // Método para activar el aumento de daño y defensa
    private void activatePowerUp() {
        this.minDamage *= 2;
        this.maxDamage *= 2;
        this.health += this.health; // Duplica la salud, solo como ejemplo
        // Puedes también agregar una lógica para restablecer las estadísticas después de 1 turno.
        System.out.println("Daño y defensa duplicados por 1 turno.");
    }

    // Método para restaurar los valores originales después del turno
    public void endPowerUp() {
        this.minDamage /= 2;
        this.maxDamage /= 2;
        this.health /= 2; // Restaura la salud
        System.out.println("Potencial Desbloqueado finalizado. Valores restaurados.");
    }
}
