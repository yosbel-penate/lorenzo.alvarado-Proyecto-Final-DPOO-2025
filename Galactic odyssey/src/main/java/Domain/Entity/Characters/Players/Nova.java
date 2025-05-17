package Domain.Entity.Characters.Players;
import Domain.Entity.Characters.Players.Hero;

class Nova extends Hero {
    public Nova() {
        super("Nova", 16, 6, 2, 1, 3, 2);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Desactivación de Trampas: desactiva trampas en un radio de 3 casillas.");
        resetCooldown();
    }
}
