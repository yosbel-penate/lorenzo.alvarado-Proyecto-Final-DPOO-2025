package Domain.Entity.Elements.Trampas;

import Domain.Entity.Characters.Players.Hero;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class TrampaAntigua implements TrampaBase {

    private int x, y;
    private boolean isVisible = false;
    private Entity entity;

    // Usamos un Set para asegurar que cada héroe solo sea afectado una vez
    private Set<Hero> heroesAfectados = new HashSet<>();

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        this.entity.getViewComponent().setOpacity(0.0);
        this.entity.addComponent(new CollidableComponent(true)); // Agregar componente de colisión
    }

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void reveal() {
        if (entity != null && !isVisible) {
            isVisible = true;
            entity.getViewComponent().setOpacity(1.0);
            System.out.println("Trampa Antigua revelada en (" + x + ", " + y + ")");
        }
    }

    @Override
    public void applyEffect(List<Hero> heroes) {
        for (Hero hero : heroes) {
            // Solo afecta al héroe que está en la casilla de la trampa
            if (hero.getX() == x && hero.getY() == y) {
                // Solo se aplica el daño si el héroe NO ha sido afectado previamente
                if (!heroesAfectados.contains(hero)) {
                    if (!isVisible) {
                        reveal(); // Revelar la trampa si no está visible
                    }
                    int damage = FXGL.random(3, 5); // Daño aleatorio entre 3 y 5
                    hero.receiveDamage(damage); // Aplicar daño al héroe
                    heroesAfectados.add(hero); // Registrar al héroe como afectado
                    System.out.println(hero.getName() + " activó la trampa antigua y recibió " + damage + " de daño.");
                } else {
                    System.out.println(hero.getName() + " ya ha sido afectado por esta trampa.");
                }
            }
        }
    }

    // Método para reiniciar el efecto al inicio de un turno (en caso de que quieras resetear los efectos).
    public void resetHeroesAfectados() {
        heroesAfectados.clear(); // Limpiar el set de héroes afectados al final del turno.
    }
}
