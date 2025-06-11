package Domain.Entity.Elements.Trampas;

import Domain.Entity.Characters.Players.Hero;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NebulosaToxica implements TrampaBase {

    private int x, y;
    private boolean isVisible = false;
    private Entity entity;
    private Set<Hero> heroesAfectados = new HashSet<>();

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2D getPosition() {
        return new Point2D(x, y);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        this.entity.getViewComponent().setOpacity(0.0);
    }

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void reveal() {
        if (entity != null && !isVisible) {
            isVisible = true;
            entity.getViewComponent().setOpacity(1.0);
            System.out.println("Nebulosa Tóxica revelada en (" + x + ", " + y + ")");
        }
    }

    public void applyEffect(List<Hero> heroes) {

        for (Hero hero : heroes) {
            int heroX = hero.getX();
            int heroY = hero.getY();

            if (heroX == x && heroY == y) {
                if (!isVisible) {
                    reveal();
                }

                // Solo aplicar efecto si no fue afectado ya estando aquí
                if (!heroesAfectados.contains(hero)) {
                    heroesAfectados.add(hero);

                    if (!hero.hasProtection()) {
                        int damage = FXGL.random(1, 2);
                        hero.receiveDamage(damage);
                        System.out.println(hero.getName() + " sufre " + damage + " de daño por la Nebulosa Tóxica.");
                    } else {
                        System.out.println(hero.getName() + " está protegido y no recibe daño.");
                    }
                }
            } else {
                // Si ya no está sobre la nebulosa, lo removemos del set
                heroesAfectados.remove(hero);
            }
        }
    }
}
