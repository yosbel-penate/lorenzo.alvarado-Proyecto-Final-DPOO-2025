package View.Maps.Map_1;

import Domain.Entity.EntityType;
import Domain.Tile.Tile; // Asegúrate de que esta clase se llame "Tile" en inglés
import com.almasb.fxgl.dsl.FXGL;

import java.util.List;
import java.util.stream.Collectors;

public class MapLoader {

    public void loadMap() {
        // Load the Tiled map
        FXGL.setLevelFromMap("MapGame1.tmx");

        // Iterate over the entities created from the map and configure them as obstacles
        FXGL.getGameWorld().getEntities()
                .stream()
                .filter(e -> e.getProperties().exists("type")
                        && "obstacle".equalsIgnoreCase(e.getString("type")))
                .forEach(e -> {
                    e.setType(EntityType.OBSTACLE);
                    // Now the GameFactory will create the view and the bounding box
                });
    }

    public List<Domain.Tile.Tile> getObstacles() {
        return FXGL.getGameWorld()
                .getEntitiesByType(EntityType.OBSTACLE)
                .stream()
                .map(entity -> new Tile((int) entity.getX(), (int) entity.getY()))
                .collect(Collectors.toList());
    }
}
