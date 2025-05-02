package View.Maps;

import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class GameFactory implements EntityFactory {

  @Spawns("obstaculo")
  public Entity newObstaculo(SpawnData data) {
    double w = ((Number) data.get("width")).doubleValue();
    double h = ((Number) data.get("height")).doubleValue();

    double x = data.getX();
    double y = data.getY() - h; // Ajuste para objetos de Tiled

    return FXGL.entityBuilder()
            .at(x, y)
            .type(EntityType.OBSTACULOS)
            .viewWithBBox(new Rectangle(w, h, Color.TRANSPARENT))
            .build();
  }
}
