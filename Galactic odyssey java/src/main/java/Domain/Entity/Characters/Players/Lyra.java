package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

class Lyra extends Hero {
    public Lyra() {
        super("Lyra", 18, 5, 4, 0, 0, 2,"lyra.png");
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Curación Cósmica: restaura 3-5 puntos de salud a aliados en un radio de 4 casillas.");
        restoreHealthToAllies();  // Llamamos al método para restaurar salud
        resetCooldown();
    }

    private void restoreHealthToAllies() {
        List<Entity> allHeroes = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);

        int centerX = getX();
        int centerY = getY();

        for (Entity entity : allHeroes) {
            if (!entity.hasComponent(HeroComponent.class))
                continue;

            HeroComponent heroComponent = entity.getComponent(HeroComponent.class);
            Hero ally = heroComponent.getHero();

            if (ally == this) continue;

            int dx = Math.abs(ally.getX() - centerX);
            int dy = Math.abs(ally.getY() - centerY);
            int distance = dx + dy;

            if (distance <= 4) {
                int healAmount = FXGL.random(3, 5);
                ally.restoreHealth(healAmount);
                System.out.println(ally.getName() + " ha sido curado por " + healAmount + " puntos.");
            }
        }

        System.out.println("Se ha restaurado salud a los aliados en un radio de 4 casillas.");
    }


}
