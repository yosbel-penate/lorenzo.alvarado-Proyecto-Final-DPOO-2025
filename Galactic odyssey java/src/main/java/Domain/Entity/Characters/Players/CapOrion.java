package Domain.Entity.Characters.Players;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;
import Domain.Entity.EntityType;

import java.util.List;

public class CapOrion extends Hero {
    private int turnsRemaining; // Para llevar el seguimiento de la duración de la habilidad
    private static final int RADIUS = 4; // Radio de 4 casillas

    public CapOrion() {
        super("Capitán Orion", 20, 5, 3, 2, 4, 3,"capOrion.png");
        turnsRemaining = 0;
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Inspiración Galáctica: aumenta el ataque y defensa de los aliados en un radio de 4 casillas durante 2 turnos.");

        // Aumentar las estadísticas de los aliados dentro del radio de 4 casillas
        List<Entity> allies = getAllAlliesInRange(RADIUS);
        if (allies == null || allies.isEmpty()) {
            System.out.println("No hay aliados en el rango para buffear.");
            return;
        }

        for (Entity ally : allies) {
            Hero allyHero = ally.getComponent(Hero.class); // Ahora puedes acceder correctamente al componente Hero
            if (allyHero != null && allyHero.isAlive()) {
                allyHero.minDamage += 1;  // Aumentar ataque
                allyHero.maxDamage += 1;  // Aumentar ataque
                // Aumentar "defensa" (esto dependería de la implementación exacta de la defensa)
                allyHero.health += 5; // Esto puede variar dependiendo de cómo implementes la defensa
            }
        }

        // Iniciar la duración de la habilidad
        turnsRemaining = 2;
        resetCooldown();
    }

    public void endTurn() {
        if (turnsRemaining > 0) {
            turnsRemaining--;
            if (turnsRemaining == 0) {
                // Revertir los efectos después de 2 turnos
                List<Entity> allies = getAllAlliesInRange(RADIUS);
                for (Entity ally : allies) {
                    Hero allyHero = ally.getComponent(Hero.class); // Ahora puedes acceder correctamente al componente Hero
                    if (allyHero != null && allyHero.isAlive()) {
                        allyHero.minDamage -= 1;  // Revertir aumento de ataque
                        allyHero.maxDamage -= 1;  // Revertir aumento de ataque
                        allyHero.health -= 5;     // Revertir aumento de "defensa"
                    }
                }
            }
        }
    }

    private List<Entity> getAllAlliesInRange(int range) {
        Entity self = this.getEntity(); // La entidad que representa a CapOrion
        if (self == null) return List.of();

        return FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).stream()
                .filter(e -> !e.equals(self)) // No incluirse a sí mismo
                .filter(e -> {
                    // Comprobación de distancia en casillas
                    int dx =(int) Math.abs(e.getX() / 56 - self.getX() / 56);
                    int dy =(int) Math.abs(e.getY() / 56 - self.getY() / 56);
                    return dx + dy <= range; // Distancia de Manhattan
                })
                .toList();
    }

}
