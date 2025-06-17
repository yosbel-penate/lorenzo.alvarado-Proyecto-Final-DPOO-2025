package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Players.Hero;
import com.almasb.fxgl.dsl.FXGL;
import Domain.Entity.EntityType;
class Nova extends Hero {
    public Nova() {
        super("Nova", 16, 6, 2, 1, 3, 2,"nova.png");
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Desactivación de Trampas: desactiva trampas en un radio de 3 casillas.");
        deactivateTraps();  // Llamamos al método para desactivar las trampas
        resetCooldown();
    }

    // Método para desactivar trampas en un radio de 3 casillas
    private void deactivateTraps() {
        int radius = 3;

        FXGL.getGameWorld().getEntitiesByType(EntityType.TRAP)
                .stream()
                .filter(trap -> {
                    int trapX = trap.getInt("x");
                    int trapY = trap.getInt("y");
                    return getDistanceTo(trapX, trapY) <= radius;
                })
                .forEach(trap -> {
                    System.out.println("Trampa desactivada en: (" + trap.getInt("x") + ", " + trap.getInt("y") + ")");
                    trap.removeFromWorld(); // O cambiar estado si no quieres eliminarla completamente
                });

        System.out.println("Todas las trampas dentro de 3 casillas han sido desactivadas.");
    }

}
