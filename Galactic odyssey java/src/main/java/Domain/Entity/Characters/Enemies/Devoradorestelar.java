package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Players.HeroComponent;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;

import java.util.List;
import java.util.Random;

public class Devoradorestelar extends Enemy {
    private int phase = 1;

    public Devoradorestelar() {
        super("El Devorador Estelar", 100, 4, 6, 6, 8, 3);
    }

    @Override
    public void useSpecialAbility() {
        if (phase == 1 && getHealth() <= 70) {
            phase = 2; // Cambiar a Fase 2
            System.out.println("Fase 2 activada: Agujero Negro.");
            useAgujeroNegro();
        } else if (phase == 2 && getHealth() <= 40) {
            phase = 3; // Cambiar a Fase 3
            System.out.println("Fase 3 activada: Explosión Final.");
            useExplosionFinal();
        } else {
            // Fase 1 o Fase 2, dependiendo del HP
            if (phase == 1) {
                useLlamaradaCosmica();
            }
        }
        resetCooldown();
    }

    // Fase 1 - Llamarada Cósmica: Daño en un radio de 4 casillas
    private void useLlamaradaCosmica() {
        System.out.println("Llamarada Cósmica: Daño a todos los personajes en un radio de 4 casillas.");

        // Obtener todos los personajes del mundo (esto depende de cómo estés organizando las entidades en tu juego)
        List<Entity> personajes = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);  // Suponiendo que los personajes tienen este tipo

        // Recorrer todos los personajes para ver si están en el rango de 4 casillas
        for (Entity personaje : personajes) {
            int distancia = getDistanceTo((int) personaje.getX(), (int)personaje.getY());  // Obtener la distancia desde el Devorador Estelar al personaje

            // Si el personaje está dentro del rango de 4 casillas, infligir daño
            if (distancia <= 4) {
                int dano = new Random().nextInt(3) + 3; // Daño aleatorio entre 3 y 5
                personaje.getComponent(HeroComponent.class).getHero().receiveDamage(dano); // Aplicar el daño
                System.out.println("Personaje " + personaje.getComponent(HeroComponent.class).getHero().getName() + " ha recibido " + dano + " de daño.");
            }
        }
    }


    // Fase 2 - Agujero Negro: Atrae y daña a los personajes cercanos
    private void useAgujeroNegro() {
        System.out.println("Agujero Negro: Crea un agujero negro que daña y atrae a los personajes.");

        // Obtener las coordenadas actuales del Devorador Estelar
        int devoradorX = getX();  // Asumiendo que tienes métodos para obtener la posición
        int devoradorY = getY();

        // Generar una casilla aleatoria dentro de un radio de 6 casillas
        int randomX = devoradorX + new Random().nextInt(13) - 6;  // Rango aleatorio de -6 a +6 en X
        int randomY = devoradorY + new Random().nextInt(13) - 6;  // Rango aleatorio de -6 a +6 en Y

        // Asegurarse de que el agujero negro esté dentro de las coordenadas válidas del mapa (opcional)
        randomX = Math.max(0, Math.min(randomX, FXGL.getAppWidth() / 56 - 1));  // Asumiendo que tileSize es el tamaño de las casillas
        randomY = Math.max(0, Math.min(randomY, FXGL.getAppHeight() / 565 - 1));

        // Crear el agujero negro en la casilla aleatoria
        FXGL.getGameWorld().spawn("agujero_negro", randomX, randomY);

        // Infligir daño a los personajes cercanos (en un radio de 1 casilla alrededor del agujero negro)
        List<Entity> personajes = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);  // O el tipo adecuado para los personajes
        for (Entity personaje : personajes) {
            int distancia = getDistanceTo((int)personaje.getX(), (int) personaje.getY());  // Distancia al personaje

            if (distancia <= 1) {  // Si está a una casilla del agujero negro
                int dano = new Random().nextInt(3) + 5; // Daño aleatorio entre 5 y 7
                personaje.getComponent(HeroComponent.class).getHero().receiveDamage(dano); // Aplicar daño
                System.out.println("Personaje " + personaje.getComponent(HeroComponent.class).getHero().getName() + " ha recibido " + dano + " de daño.");

                // Atraer el personaje hacia el agujero negro (moverlo a las coordenadas del agujero)
                moveCharacterToAgujero(personaje, randomX, randomY);
            }
        }
    }

    // Método para mover el personaje hacia el agujero negro
    private void moveCharacterToAgujero(Entity personaje, int agujeroX, int agujeroY) {
        // Mover el personaje a la posición del agujero negro
        personaje.setPosition(agujeroX, agujeroY);
        System.out.println("Personaje " + personaje.getComponent(HeroComponent.class).getHero().getName() + " ha sido atraído al agujero negro.");
    }


    // Fase 3 - Explosión Final: Gran daño en un radio de 8 casillas


    private void useExplosionFinal() {
        System.out.println("Colapso Gravitacional: Afectando personajes cercanos...");

        Entity devoradorEstelarEntity = FXGL.getGameWorld().getEntitiesByType(EntityType.DEVORADOR_ESTELAR).get(0);
        TransformComponent transformDevorador = devoradorEstelarEntity.getComponent(TransformComponent.class);

        if (transformDevorador != null) {
            int xDevorador = (int) transformDevorador.getX();
            int yDevorador = (int) transformDevorador.getY();

            // Aquí puedes usar xDevorador y yDevorador para encontrar personajes dentro de cierto radio
            // y aplicar el efecto de la habilidad.
        }

        resetCooldown();  // Si aplica
    }



    // Método para actualizar el estado del Devorador Estelar al inicio de cada turno
    public void updateStatus() {
        // Aquí puedes agregar la lógica para verificar las fases y cambiar de fase si es necesario
        if (getHealth() <= 70 && phase == 1) {
            phase = 2;
            System.out.println("Fase 2 activada.");
        }
        if (getHealth() <= 40 && phase == 2) {
            phase = 3;
            System.out.println("Fase 3 activada.");
        }
    }
}
