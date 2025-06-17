package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.Characters.Players.Hero;
import com.almasb.fxgl.dsl.FXGL;

import java.util.List;
import java.util.Random;

public class PirataEspacial extends Enemy {

    public PirataEspacial() {
        super("Pirata Espacial", 20, 5, 4, 2, 5, 3);
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Robo: roba un objeto aleatorio de un personaje.");

        // Obtener todos los héroes cercanos en un rango de 2 casillas
        List<Hero> heroes = FXGL.<List<Hero>>geto("heroes"); // Asegúrate que tienes esta propiedad global
        for (Hero hero : heroes) {
            int dist = Math.abs(hero.getX() - getX()) + Math.abs(hero.getY() - getY());
            if (dist <= 2) {
                // Simular el robo: quitar algo útil (como escudo, ocultamiento, inteligencia, etc.)
                Random rand = new Random();
                int efecto = rand.nextInt(3);
                switch (efecto) {
                    case 0:
                        // Quitar escudo
                        hero.addShield(-4);
                        System.out.println(getName() + " le roba el escudo a " + hero.getName());
                        break;
                    case 1:
                        // Quitar ocultamiento
                        if (hero.isHidden()) {
                            hero.setHidden(false);
                            System.out.println(getName() + " revela a " + hero.getName() + " y lo saca de su escondite.");
                        }
                        break;
                    case 2:
                        // Robar inteligencia (reduce inteligencia del héroe)
                        hero.increaseIntelligence(-1);
                        System.out.println(getName() + " roba conocimiento de " + hero.getName());
                        break;
                }
                break; // Solo roba a un héroe
            }
        }

        resetCooldown();
    }
}
