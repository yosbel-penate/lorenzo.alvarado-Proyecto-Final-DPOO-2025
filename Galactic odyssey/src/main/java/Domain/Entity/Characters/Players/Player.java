package Domain.Entity.Characters.Players;

import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {
    private Entity player;
    private final int TILE_SIZE = 32;

    public void spawnPlayer() {
        // Se crea el jugador con vista, hitbox (implícito en viewWithBBox) y componente de física
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(480, 480)
                .viewWithBBox(new Rectangle(TILE_SIZE, TILE_SIZE, Color.GREEN))
                .with(new PhysicsComponent())
                .buildAndAttach();

        // Vincular la cámara al jugador
        FXGL.getGameScene().getViewport()
                .bindToEntity(player, FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);

        initInput();
    }

    public void moveByGrid(int dx, int dy) {
        // Calcula la nueva posición en función de la cuadrícula y mueve al jugador sin verificación de colisiones
        double newX = player.getX() + dx * TILE_SIZE;
        double newY = player.getY() + dy * TILE_SIZE;
        player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(newX, newY));
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

    public Entity getEntity() {
        return player;
    }
}
