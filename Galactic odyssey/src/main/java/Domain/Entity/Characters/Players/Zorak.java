package Domain.Entity.Characters.Players;

class Zorak extends Hero {
    public Zorak() {
        super("Zorak", 24, 4, 1, 4, 6, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Golpe de Energía: daña a todos los enemigos en un radio de 2 casillas.");
        resetCooldown();
    }
}