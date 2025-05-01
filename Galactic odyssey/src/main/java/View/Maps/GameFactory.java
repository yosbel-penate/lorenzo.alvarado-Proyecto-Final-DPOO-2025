package View.Maps;

import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameFactory implements EntityFactory {

  @Spawns("obstaculo")
  public Entity newObstaculo(SpawnData data) {
    // Creación de un obstáculo simple sin detección de colisiones ni componentes físicos
    return FXGL.entityBuilder()
            .type(EntityType.OBSTACULOS)
            .at(data.getX(), data.getY())
            .build();
  }
}
