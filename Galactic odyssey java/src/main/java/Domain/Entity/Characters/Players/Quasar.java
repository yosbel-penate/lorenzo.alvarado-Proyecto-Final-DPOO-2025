package Domain.Entity.Characters.Players;

public class Quasar extends Hero {
    public Quasar() {
        super("Quasar", 17, 5, 3, 1, 2, 3, "quasar.png");
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Reparación Rápida: restaura 2-4 puntos de armadura.");

        // Restauramos armadura usando el método de la clase Hero
        int restoredArmor = (int)(Math.random() * 3) + 2;  // Genera un valor aleatorio entre 2 y 4
        restoreArmor(restoredArmor);  // Llama al método de la clase Hero

        resetCooldown();
    }
}
