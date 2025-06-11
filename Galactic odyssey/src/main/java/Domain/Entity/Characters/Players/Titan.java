package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;
import java.util.List;
import com.almasb.fxgl.dsl.FXGL;
import Domain.Entity.EntityType;
public class Titan extends Hero {

    public Titan() {
        super("Titan", 30, 3, 1, 2, 3, 4);
    }

    @Override
    public void useSpecialAbility() {
        // Encuentra aliados en un radio de 2 casillas (puede incluirse a sí mismo)
        List<Hero> allies = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.HERO)
                .stream()
                .map(e -> e.getComponent(Hero.class))
                .filter(hero -> hero != null && getDistanceTo(hero.getX(), hero.getY()) <= 2)
                .toList();

        for (Hero ally : allies) {
            ally.addShield(5);
            System.out.println(ally.getName() + " recibió un escudo de energía (5 pts).");
        }

        resetCooldown();
    }
}
