package App;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {
    private Entity player;
    private PhysicsComponent physics;
    private final int TILE_SIZE = 32;

    public void spawnPlayer() {

        // Construcción de la entidad jugable
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(100, 100)
                .view(new Rectangle(32, 32, Color.BLUE))
                .bbox(new HitBox(BoundingShape.box(32, 32)))
                .with(new CollidableComponent(true))
                .buildAndAttach();


        // Vincular cámara al jugador
        FXGL.getGameScene().getViewport()
                .bindToEntity(player,
                        FXGL.getAppWidth() /2.0,
                        FXGL.getAppHeight() / 2.0);
    }

    public void initInput() {
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                moveByGrid(1, 0);
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                moveByGrid(-1, 0);
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onActionBegin() {
                moveByGrid(0, -1);
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                moveByGrid(0, 1);
            }
        }, KeyCode.S);

    }

    private boolean isBlocked(int x, int y) {
        return FXGL.getGameWorld().getEntitiesAt(new Point2D(x + 16, y + 16)).stream()
                .anyMatch(e -> e.isType(EntityType.OBSTACULOS));
    }

//implementar movimiento por casillas

    public void moveByGrid(int dx, int dy) {
        int newX = (int) player.getX() + dx * TILE_SIZE;
        int newY = (int) player.getY() + dy * TILE_SIZE;

        // Comprobar colisiones antes de mover (opcional)
        if (!isBlocked(newX, newY)) {
            player.setX(newX);
            player.setY(newY);
        }
    }

    public Entity getEntity() {
        return player;
    }
}
