package View.Maps;

import Domain.Entity.Characters.Enemies.*;
import Domain.Entity.Elements.ObjetosMagicos.Item;
import Domain.Entity.Elements.ObjetosMagicos.ItemEntity;
import Domain.Entity.Elements.Trampas.TrampaBase;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class GameFactory implements EntityFactory {

  private static final int TILE_SIZE = 56;
  private static final int VISUAL_OFFSET_X = 0;  // Ajusta si necesitas offset en X
  private static final int VISUAL_OFFSET_Y = 0;  // Ajusta si necesitas offset en Y

  // Mapa para controlar posiciones ocupadas, si lo necesitas para lógica de spawn
  public static final Map<Point2D, Entity> POSITION_MAP = new HashMap<>();

  @Spawns("obstaculo")
  public Entity newObstaculo(SpawnData data) {
    double width = ((Number) data.get("width")).doubleValue();
    double height = ((Number) data.get("height")).doubleValue();

    double x = data.getX();
    double y = data.getY();

    PhysicsComponent physics = new PhysicsComponent();
    physics.setBodyType(BodyType.STATIC);

    return FXGL.entityBuilder()
            .at((int) x * TILE_SIZE, (int) y * TILE_SIZE)
            .type(EntityType.OBSTACULOS)
            .bbox(new HitBox("OBSTACLE_HITBOX", BoundingShape.box(width, height))) // ✅ BoundingShape en vez de Rectangle
            .collidable()
            .with(physics)
            .with(new CollidableComponent(true))
            .collidable()
            .build();
  }
  @Spawns("hero")
  public Entity spawnHero(SpawnData data) {
    String heroType = data.get("type");

    PhysicsComponent physics = new PhysicsComponent();
    physics.setBodyType(BodyType.DYNAMIC);  // Personaje dinámico que se mueve y colisiona

    return FXGL.entityBuilder(data)
            .type(EntityType.HERO)
            .viewWithBBox(heroType + ".png")
            .bbox(new HitBox("Body", BoundingShape.box(40, 40))) // Ajusta el tamaño según tu imagen
            .collidable()
            .with(physics)
            .with(new CollidableComponent(true))
            .zIndex(100)
            .build();
  }
  @Spawns("enemy")
  public Entity spawnEnemy(SpawnData data) {
    Enemy enemyLogic = data.get("logic");
    String enemyType = data.get("type");

    double tileX = data.getX();
    double tileY = data.getY();

    Point2D tilePos = new Point2D(tileX, tileY);
    enemyLogic.setPosition((int) tileX, (int) tileY);

    PhysicsComponent physics = new PhysicsComponent();
    physics.setBodyType(BodyType.KINEMATIC);  // O STATIC si el enemigo no se mueve físicamente

    Entity enemyEntity = FXGL.entityBuilder()
            .type(EntityType.ENEMY)
            .at(tileX * TILE_SIZE + VISUAL_OFFSET_X, tileY * TILE_SIZE + VISUAL_OFFSET_Y)
            .viewWithBBox(enemyType + ".png")
            .collidable()
            .with(physics)  // <--- Aquí lo agregas
            .with(new CollidableComponent(true))
            .with(new EnemyComponent(enemyLogic))
            .zIndex((int) (tileY * TILE_SIZE))
            .buildAndAttach();

    POSITION_MAP.put(tilePos, enemyEntity);

    return enemyEntity;
  }


  @Spawns("trampa")
  public Entity spawnTrampa(SpawnData data) {
    TrampaBase trampa = (TrampaBase) data.get("elemento");
    String spriteName = data.get("spriteName");

    double x = data.getX() * TILE_SIZE + VISUAL_OFFSET_X;
    double y = data.getY() * TILE_SIZE + VISUAL_OFFSET_Y;

    Entity entity = FXGL.entityBuilder()
            .at(x, y)
            .viewWithBBox(FXGL.texture(spriteName))
            .build();

    // Comenzar invisible
    entity.getViewComponent().setOpacity(0.0);

    trampa.setEntity(entity);

    return entity;
  }

  @Spawns("item")
  public Entity newItem(SpawnData data) {
    Item item = (Item) data.get("item");

    double x = data.getX() * TILE_SIZE + VISUAL_OFFSET_X;
    double y = data.getY() * TILE_SIZE + VISUAL_OFFSET_Y;

    return FXGL.entityBuilder()
            .type(EntityType.ITEM)
            .at(x, y)
            .viewWithBBox(item.getName() + ".png")
            .with(new CollidableComponent(true))
            .with(new ItemEntity(item))
            .zIndex(50)
            .build();
  }
}
