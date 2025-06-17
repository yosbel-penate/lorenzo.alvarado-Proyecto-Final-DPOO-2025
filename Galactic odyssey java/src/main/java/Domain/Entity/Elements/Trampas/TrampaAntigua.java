package Domain.Entity.Elements.Trampas;

import Domain.Entity.Characters.Players.Hero;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;

import java.util.List;

public class TrampaAntigua implements TrampaBase {

    private int x, y;
    private boolean isVisible = false;
    private boolean yaActivada = false;
    private Entity entity;

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        this.entity.getViewComponent().setOpacity(0.0);
        this.entity.addComponent(new CollidableComponent(true));
    }

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void reveal() {
        if (entity != null && !isVisible) {
            isVisible = true;
            entity.getViewComponent().setOpacity(1.0);
            System.out.println("🟡 Trampa Antigua revelada en (" + x + ", " + y + ")");
        }
    }

    @Override
    public void applyEffect(List<Hero> heroes) {
        System.out.println("📛 applyEffect llamado | yaActivada = " + yaActivada);

        if (yaActivada) return;

        for (Hero hero : heroes) {
            if (hero.getX() == x && hero.getY() == y) {
                System.out.println("✅ Héroe en posición de la trampa");

                if (!isVisible) {
                    reveal();
                }

                int damage = FXGL.random(3, 5);
                hero.receiveDamage(damage);
                yaActivada = true;

                System.out.println("🔥 " + hero.getName() + " activó la trampa y recibió " + damage + " de daño.");
                return; // No seguimos revisando más héroes
            }
        }
    }
}
