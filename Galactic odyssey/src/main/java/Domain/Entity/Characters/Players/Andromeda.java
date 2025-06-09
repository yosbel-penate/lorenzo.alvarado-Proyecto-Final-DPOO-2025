package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.HeroComponent;
import Domain.Entity.Characters.Enemies.EnemyComponent;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

public class Andromeda extends Hero {

    public Andromeda() {
        super("Andrómeda", 20, 5, 6, 3, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Ritual Estelar: cura aliados o daña enemigos (5-7 curación o 4-6 daño).");

        int radio = 4;
        int healMin = 5, healMax = 7;
        int dmgMin = 4, dmgMax = 6;

        List<Entity> allEntities = FXGL.getGameWorld().getEntitiesCopy();

        for (Entity e : allEntities) {
            // Si es aliado (jugador)
            if (e.isType(EntityType.PLAYER) && e.hasComponent(HeroComponent.class)) {
                Hero aliado = e.getComponent(HeroComponent.class).getHero();
                int dist = getDistanceTo(aliado.getX(), aliado.getY());

                if (!aliado.equals(this) && dist <= radio && aliado.isAlive()) {
                    int healing = (int)(Math.random() * (healMax - healMin + 1)) + healMin;
                    aliado.health += healing;
                    System.out.println("Andrómeda cura a " + aliado.name + " por " + healing + ". Vida: " + aliado.health);
                }

                // Si es enemigo
            } else if (e.isType(EntityType.ENEMY) && e.hasComponent(EnemyComponent.class)) {
                var enemigo = e.getComponent(EnemyComponent.class).getEnemy();
                int dist = getDistanceTo(enemigo.getX(), enemigo.getY());

                if (dist <= radio && enemigo.isAlive()) {
                    int damage = (int)(Math.random() * (dmgMax - dmgMin + 1)) + dmgMin;
                    enemigo.health -= damage;
                    System.out.println("Andrómeda daña a " + enemigo.name + " por " + damage + ". Vida: " + enemigo.health);

                    if (!enemigo.isAlive()) {
                        FXGL.getGameWorld().removeEntity(e);
                        System.out.println(enemigo.name + " ha sido eliminado por Andrómeda.");
                    }
                }
            }
        }

        resetCooldown();
    }
}
