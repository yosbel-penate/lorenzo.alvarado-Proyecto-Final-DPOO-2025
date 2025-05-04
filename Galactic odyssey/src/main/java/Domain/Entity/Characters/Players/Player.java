package Domain.Entity.Characters.Players;

import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionResult;
import com.almasb.fxgl.physics.HitBox;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Player {
    private Entity player;
    private final int TILE_SIZE = 32;

    public void spawnPlayer() {
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(287, 448)
                .view(FXGL.texture("personaje.png", 32, 64)) // Imagen entera
                .bbox(new HitBox("FOOTBOX", BoundingShape.box(32, 64))) // Solo la parte inferior (pies)
                .buildAndAttach();


        // Vincular la cámara al jugador
        FXGL.getGameScene().getViewport()
                .bindToEntity(player, FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);

        initInput();
    }

    public void moveByGrid(int dx, int dy) {
        double newX = player.getX() + dx * TILE_SIZE;
        double newY = player.getY() + dy * TILE_SIZE;

        // Usamos el ancho/alto real del jugador
        double pw = player.getWidth();
        double ph = player.getHeight();

        // Dimension futura del jugador
        Rectangle2D futureBB = new Rectangle2D(newX, newY, pw, ph);

        List<Entity> obstacles = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.OBSTACULOS);

        boolean blocked = obstacles.stream().anyMatch(o -> {
            // Y el ancho/alto real del obstáculo
            double ow = o.getWidth();
            double oh = o.getHeight();
            Rectangle2D obBB = new Rectangle2D(o.getX(), o.getY(), ow, oh);
            return futureBB.intersects(obBB);
        });

        if (!blocked) {
            player.setPosition(newX, newY);
        }
    }


    private void initInput() {
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

    public Entity getEntity() {
        return player;
    }
}
