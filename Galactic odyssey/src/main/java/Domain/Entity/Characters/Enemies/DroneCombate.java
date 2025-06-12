package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.Characters.Players.HeroComponent;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

public class DroneCombate extends Enemy {
    public DroneCombate() {
        super("Drone de Combate", 12, 4, 3, 2, 3, 3);
    }

    @Override

    public void useSpecialAbility() {
        List<Entity> heroes = FXGL.getGameWorld().getEntitiesByType(EntityType.HERO);

        boolean foundHidden = false;

        for (Entity entity : heroes) {
            Hero hero = entity.getComponent(HeroComponent.class).getHero();

            if (getDistanceTo(hero.getX(), hero.getY()) <= 5 && hero.isHidden()) {
                hero.setHidden(false); // actualizar estado lógico
                entity.setVisible(true); // mostrar visualmente
                entity.getViewComponent().setOpacity(1.0); // por si estaba atenuado

                // Aquí podrías agregar un efecto más llamativo si deseas
                FXGL.getGameScene().getViewport().shakeTranslational(0.3); // pequeño temblor

                foundHidden = true;
                System.out.println(getName() + " ha detectado a " + hero.getName() + " con Escaneo.");
            }
        }

        if (!foundHidden) {
            System.out.println(getName() + " usó Escaneo, pero no detectó a nadie.");
        }

        resetCooldown();
    }

}
