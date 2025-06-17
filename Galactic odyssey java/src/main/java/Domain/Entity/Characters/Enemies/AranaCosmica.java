package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.Characters.Players.HeroStatusEffect;
import java.util.List;

public class AranaCosmica extends Enemy {
    private List<Hero> heroes;

    public AranaCosmica(List<Hero> heroes) {
        super("Araña Cósmica", 18, 3, 2, 3, 4, 4);
        this.heroes = heroes;
    }

    @Override
    public void useSpecialAbility() {
        boolean atrapado = false;

        for (Hero hero : heroes) {
            int distance = getDistanceTo(hero.getX(), hero.getY());
            if (distance <= 3 && hero.isAlive()) {
                hero.addStatusEffect(HeroStatusEffect.STUNNED); // o TRAPPED si creas ese enum
                System.out.println(name + " ha atrapado a " + hero.getName() + " con Red de Energía.");
                atrapado = true;
                break; // Solo atrapa a uno
            }
        }

        if (!atrapado) {
            System.out.println(name + " intentó usar Red de Energía, pero no había héroes en rango.");
        }

        resetCooldown();
    }
}
