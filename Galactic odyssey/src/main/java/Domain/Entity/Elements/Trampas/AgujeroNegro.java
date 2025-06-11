package Domain.Entity.Elements.Trampas;

import Domain.Entity.Characters.Players.Hero;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class AgujeroNegro implements TrampaBase {

    private int x, y;
    private boolean isVisible = false;
    private Entity entity;

    // Set para controlar qué héroes ya recibieron daño
    private Set<Hero> heroesAfectados = new HashSet<>();

    // Setters y getters para la posición
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        this.entity.getViewComponent().setOpacity(0.0);  // Comienza invisible
    }

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void reveal() {
        if (entity != null && !isVisible) {
            isVisible = true;
            entity.getViewComponent().setOpacity(1.0);  // Se vuelve visible
            System.out.println("Agujero Negro revelado en (" + x + ", " + y + ")");
        }
    }

    public void applyEffect(List<Hero> heroes) {
        for (Hero hero : heroes) {
            int dist = Math.abs(hero.getX() - x) + Math.abs(hero.getY() - y);

            // Revelar solo si está a 2 casillas de distancia
            if (dist <= 0 && !isVisible) {
                reveal();
            }

            // Si está dentro del rango de 2 casillas
            if (dist <= 2) {
                // Mover al héroe hacia el agujero negro
                hero.moveTo(x, y);  // Mueve al héroe a la posición (x, y)


                // Aplicar daño solo una vez por héroe al llegar
                if (hero.getX() == x && hero.getY() == y) {
                    if (!heroesAfectados.contains(hero)) {
                        heroesAfectados.add(hero);

                        int damage = FXGL.random(4, 6);  // Daño aleatorio entre 4 y 6
                        hero.receiveDamage(damage);
                        System.out.println(hero.getName() + " fue atraído por un agujero negro y sufrió " + damage + " de daño.");
                    }
                }
            } else {
                // Si el héroe está fuera del rango de atracción, también lo quitamos del set
                heroesAfectados.remove(hero);
            }
        }
    }

}


