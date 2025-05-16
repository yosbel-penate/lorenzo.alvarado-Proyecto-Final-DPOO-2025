package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.EnemyEntity;
import Domain.Entity.Combat.TurnManager;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

public class PlayerEntity {
    private Entity player;
    private final int TILE_SIZE = 56;
    private Hero hero;
    private int movimientosDisponibles;
    private TurnManager turnManager;
    private int mapWidth = 60 * 56;
    private int mapHeight = 30 * 56;
    private int gridX;
    private int gridY;
    private static boolean inputsRegistered = false;

    public PlayerEntity(String heroType, int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;

        switch (heroType) {
            case "Andrómeda": hero = new Andromeda(); break;
            case "Aurora": hero = new Aurora(); break;
            case "Blaze": hero = new Blaze(); break;
            case "Capitán Orion": hero = new CapOrion(); break;
            case "Comet": hero = new Comet(); break;
            case "Cygnus": hero = new Cygnus(); break;
            case "Deimos": hero = new Deimos(); break;
            case "Eclipse": hero = new Eclipse(); break;
            case "Lyra": hero = new Lyra(); break;
            case "Luna": hero = new Luna(); break;
            case "Nebula": hero = new Nebula(); break;
            case "Nova": hero = new Nova(); break;
            case "Orion Jr.": hero = new OrionJr(); break;
            case "Phobos": hero = new Phobos(); break;
            case "Quasar": hero = new Quasar(); break;
            case "Solara": hero = new Solara(); break;
            case "Stella": hero = new Stella(); break;
            case "Titan": hero = new Titan(); break;
            case "Vortex": hero = new Vortex(); break;
            case "Zorak": hero = new Zorak(); break;
            default: hero = new CapOrion(); break;
        }
    }

    public void setTurnManager(TurnManager manager) {
        this.turnManager = manager;
    }

    public void startTurn() {
        movimientosDisponibles = hero.moveRange;
        player.getViewComponent().setOpacity(1.0);
        focusCamera();

        Text text = new Text("Turno de " + hero.name);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", 24));
        text.setEffect(new DropShadow(4, Color.BLACK));

        Rectangle background = new Rectangle(300, 50);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(Color.web("#0a0e3f", 0.8));

        StackPane notification = new StackPane(background, text);
        notification.setTranslateX(250);
        notification.setTranslateY(40);
        notification.setOpacity(0);

        FXGL.getGameScene().addUINode(notification);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), notification);
        fadeIn.setToValue(1);
        fadeIn.play();

        PauseTransition wait = new PauseTransition(Duration.seconds(1));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), notification);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> FXGL.getGameScene().removeUINode(notification));

        wait.setOnFinished(e -> fadeOut.play());
        wait.play();
    }

    public void endTurn() {
        movimientosDisponibles = 0;
        player.getViewComponent().setOpacity(0.6);
    }

    public void spawnPlayer() {
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(gridX * TILE_SIZE, gridY * TILE_SIZE)
                .view(FXGL.texture("personaje.png", 56, 56))
                .bbox(new HitBox("FOOTBOX", BoundingShape.box(56, 56)))
                .buildAndAttach();

        hero.setPosition(gridX, gridY);

        if (!inputsRegistered) {
            initInput();
            inputsRegistered = true;
        }
    }

    private void focusCamera() {
        FXGL.getGameScene().getViewport().unbind();
        FXGL.getGameScene().getViewport().bindToEntity(player, FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);
        FXGL.getGameScene().getViewport().setBounds(0, 0, mapWidth, mapHeight);

        // Añade zoom (valores menores a 1.0 hacen zoom out)
        FXGL.getGameScene().getViewport().setZoom(0.6); // Puedes ajustar este valor (0.5 sería más lejos, 0.8 más cerca)
    }

    private void moveByGrid(int dx, int dy) {
        if (!turnManager.isPlayerTurn() ||
                movimientosDisponibles <= 0 ||
                turnManager.getCurrentPlayer() != this) {
            return;
        }

        double newX = player.getX() + dx * TILE_SIZE;
        double newY = player.getY() + dy * TILE_SIZE;

        List<Entity> obstacles = FXGL.getGameWorld().getEntitiesByType(EntityType.OBSTACULOS);
        Rectangle2D futureBB = new Rectangle2D(newX, newY, player.getWidth(), player.getHeight());

        boolean blocked = obstacles.stream().anyMatch(o -> {
            Rectangle2D obBB = new Rectangle2D(o.getX(), o.getY(), o.getWidth(), o.getHeight());
            return futureBB.intersects(obBB);
        });

        if (!blocked) {
            player.setPosition(newX, newY);
            hero.setPosition(hero.getX() + dx, hero.getY() + dy);
            movimientosDisponibles--;
        }
    }

    private void initInput() {
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn()) {
                    currentPlayer.moveByGrid(1, 0);
                }
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn()) {
                    currentPlayer.moveByGrid(-1, 0);
                }
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn()) {
                    currentPlayer.moveByGrid(0, -1);
                }
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn()) {
                    currentPlayer.moveByGrid(0, 1);
                }
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("End Turn") {
            @Override
            protected void onActionBegin() {
                if (turnManager.isPlayerTurn() && turnManager.getCurrentPlayer() != null) {
                    turnManager.getCurrentPlayer().endTurn();
                    turnManager.endTurn();
                }
            }
        }, KeyCode.ENTER);

        FXGL.getInput().addAction(new UserAction("Attack") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn()) {
                    for (EnemyEntity enemy : turnManager.getEnemies()) {
                        if (enemy.getLogic().isAlive()) {
                            int ex = enemy.getLogic().getX();
                            int ey = enemy.getLogic().getY();
                            int distance = currentPlayer.getHero().getDistanceTo(ex, ey);

                            if (distance <= currentPlayer.getHero().attackRange) {
                                int damage = (int)(Math.random() * (currentPlayer.getHero().maxDamage - currentPlayer.getHero().minDamage + 1)) + currentPlayer.getHero().minDamage;
                                enemy.getLogic().health -= damage;
                                enemy.showDamage(damage);

                                if (!enemy.getLogic().isAlive()) {
                                    FXGL.getGameWorld().removeEntity(enemy.getEntity());
                                    turnManager.removeEnemy(enemy);
                                }

                                currentPlayer.endTurn();
                                turnManager.endTurn();
                                break;
                            }
                        }
                    }
                }
            }
        }, KeyCode.SPACE);

        FXGL.getInput().addAction(new UserAction("Special Ability") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn() &&
                        currentPlayer.getHero().canUseSpecial()) {
                    currentPlayer.getHero().useSpecialAbility();
                    currentPlayer.getHero().resetCooldown();
                    currentPlayer.endTurn();
                    turnManager.endTurn();
                }
            }
        }, KeyCode.E);
    }

    public Entity getEntity() {
        return player;
    }

    public Hero getHero() {
        return hero;
    }
}
