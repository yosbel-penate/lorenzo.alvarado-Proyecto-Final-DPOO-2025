package App;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.entity.components.CollidableComponent;


public class GameFactory implements EntityFactory {

  @Spawns("obstaculo")
  public Entity newObstaculo(SpawnData data) {
    return FXGL.entityBuilder(data)
            .type(EntityType.OBSTACULOS)
            .bbox(new HitBox(BoundingShape.box(32, 32)))
            .with(new CollidableComponent(true))
            .build();
  }
}