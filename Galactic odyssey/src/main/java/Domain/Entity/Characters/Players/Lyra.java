package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

class Lyra extends Hero {
    public Lyra() {
        super("Lyra", 18, 5, 4, 0, 0, 2);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Curación Cósmica: restaura 3-5 puntos de salud a aliados en un radio de 4 casillas.");
        restoreHealthToAllies();  // Llamamos al método para restaurar salud
        resetCooldown();
    }

    // Método para restaurar salud a los aliados
    private void restoreHealthToAllies() {
        List<Entity> allHeroes = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);

        int centerX = getX();
        int centerY = getY();

        for (Entity entity : allHeroes) {
            HeroComponent heroComponent = entity.getComponent(HeroComponent.class);

            if (heroComponent != null) {
                Hero ally = heroComponent.getHero();

                // Evitar que se cure a sí mismo (opcional)
                if (ally == this) continue;

                // Calcular distancia
                int dx = Math.abs(ally.getX() - centerX);
                int dy = Math.abs(ally.getY() - centerY);
                int distance = dx + dy;

                if (distance <= 4) {
                    // Restaurar entre 3 y 5 puntos de salud
                    int healAmount = FXGL.random(3, 5);
                    ally.restoreHealth(healAmount);
                    System.out.println(ally.getName() + " ha sido curado por " + healAmount + " puntos.");
                }
            }
        }

        System.out.println("Se ha restaurado salud a los aliados en un radio de 4 casillas.");
    }

}
