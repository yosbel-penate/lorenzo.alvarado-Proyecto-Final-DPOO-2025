package View.Maps.Map_1;

import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;

public class MapLoader {
    public void loadMap() {
        // Cargar el mapa exportado desde Tiled
        FXGL.setLevelFromMap("MapGame1.tmx");

        // Iterar sobre las entidades creadas desde el mapa y configurarlas como obstáculos sin colisiones ni física
        FXGL.getGameWorld().getEntities()
                .stream()
                .filter(e -> e.getProperties().exists("type") && "obstaculo".equalsIgnoreCase(e.getString("type")))
                .forEach(e -> {
                    e.setType(EntityType.OBSTACULOS);
                    // En esta versión se elimina la adición de hitbox, componente colisionable y física.
                });
    }
}
