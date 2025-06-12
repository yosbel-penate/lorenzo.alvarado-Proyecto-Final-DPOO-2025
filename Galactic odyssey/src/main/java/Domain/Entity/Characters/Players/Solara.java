package Domain.Entity.Characters.Players;

public class Solara extends Hero {
    public Solara() {
        super("Solara", 25, 4, 1, 2, 3, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Muralla de Luz: bloquea daño a aliados en 2 casillas.");

        // Aplica el efecto de estado "SHIELDED"
        addStatusEffect(HeroStatusEffect.SHIELDED);

        resetCooldown();  // Reinicia el cooldown de la habilidad especial
    }
}
