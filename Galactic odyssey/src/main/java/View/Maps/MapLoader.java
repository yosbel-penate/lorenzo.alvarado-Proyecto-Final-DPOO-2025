package View.Maps;

import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;

public class MapLoader {
    public void loadMap(String nameMap) {
        // Cargar el mapa de Tiled
        FXGL.setLevelFromMap(nameMap);

        // Iterar sobre las entidades creadas desde el mapa y configurarlas como obstáculos
        FXGL.getGameWorld().getEntities()
                .stream()
                .filter(e -> e.getProperties().exists("type")
                        && "obstaculo".equalsIgnoreCase(e.getString("type")))
                .forEach(e -> {
                    e.setType(EntityType.OBSTACULOS);
                    // Ahora el GameFactory creará la vista y el bounding box
                });
    }
}
