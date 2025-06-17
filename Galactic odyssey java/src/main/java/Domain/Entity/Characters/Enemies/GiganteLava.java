package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.Characters.Players.HeroComponent;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

public class GiganteLava extends Enemy {
    public GiganteLava() {
        super("Gigante de Lava", 28, 2, 1, 5, 7, 4);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("¡" + getName() + " activa su habilidad especial: Erupción!");
        System.out.println("Daña a todos los personajes en un radio de 3 casillas (4 daño).");

        List<Entity> heroes = FXGL.getGameWorld().getEntitiesByType(EntityType.HERO);

        for (Entity entity : heroes) {
            Hero hero = entity.getComponent(HeroComponent.class).getHero();

            int distance = getDistanceTo(hero.getX(), hero.getY());

            if (distance <= 3) {
                hero.receiveDamage(4); // Aplica daño
                System.out.println("→ " + hero.getName() + " ha recibido 4 puntos de daño por la Erupción.");
            }
        }

        resetCooldown();
    }

}