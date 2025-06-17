package Domain.Entity.Characters.Players;

import com.almasb.fxgl.entity.component.Component;

public class HeroComponent extends Component {
    private Hero hero;

    public HeroComponent() {
        // Constructor vacío requerido por FXGL
    }

    public HeroComponent(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    // Método para aplicar daño
    public void takeDamage(int damage) {
        if (hero != null) {
            hero.receiveDamage(damage);  // Llamamos al método takeDamage de la clase Hero
        }
    }
}
