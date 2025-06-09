package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Players.PlayerEntity;
import Domain.Entity.Combat.TurnManager;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;

public class EnemyEntity {
    private Entity enemyEntity;
    private Enemy logic;
    private List<PlayerEntity> targets;
    private final int TILE_SIZE = 56;
    private TurnManager turnManager;
    private Random random = new Random();
    private double visualOffsetX;
    private double visualOffsetY;
    private static final double MAX_OFFSET = 8.0;
    private static final Map<Point2D, EnemyEntity> POSITION_MAP = new HashMap<>();

    public EnemyEntity(Enemy logic, List<PlayerEntity> targets, TurnManager manager) {
        this.logic = logic;
        this.targets = new ArrayList<>(targets);
        this.turnManager = manager;
        assignVisualOffset();
    }


    private void assignVisualOffset() {
        this.visualOffsetX = (random.nextDouble() - 0.5) * MAX_OFFSET;
        this.visualOffsetY = (random.nextDouble() - 0.5) * MAX_OFFSET;
    }



    public void takeTurn(Runnable onTurnComplete) {
        if (!logic.isAlive()) {
            onTurnComplete.run();
            return;
        }

        PlayerEntity target = findClosestAliveTarget();
        if (target == null) {
            onTurnComplete.run();
            return;
        }

        int distance = logic.getDistanceTo(target.getHero().getX(), target.getHero().getY());

        if (distance <= logic.attackRange) {
            int damage = random(logic.minDamage, logic.maxDamage);
            target.getHero().takeDamage(damage);
            showDamage(damage);

            System.out.println("[ENEMY] " + logic.name + " ataca a " + target.getHero().name +
                    " en (" + target.getHero().getX() + ", " + target.getHero().getY() + ")");

            FXGL.getGameTimer().runOnceAfter(onTurnComplete, Duration.seconds(0.5));

        } else if (distance <= 10) {
            System.out.println("[ENEMY] " + logic.name + " se moverá hacia " +
                    target.getHero().name + " en (" + target.getHero().getX() + ", " + target.getHero().getY() + ")");

            moveTowardPlayer(target, onTurnComplete);

        } else {
            System.out.println("[ENEMY] " + logic.name + " está patrullando.");
            patrol(onTurnComplete);
        }
    }



    private PlayerEntity findClosestAliveTarget() {
        return targets.stream()
                .filter(p -> p.getHero().isAlive())
                .min(Comparator.comparingInt(p -> logic.getDistanceTo(p.getHero().getX(), p.getHero().getY())))
                .orElse(null);
    }

    private void moveTowardPlayer(PlayerEntity target, Runnable onTurnComplete) {
        Point2D currentPos = new Point2D(logic.getX(), logic.getY());

        List<Point2D> possibleMoves = Arrays.asList(
                new Point2D(currentPos.getX() + 1, currentPos.getY()),
                new Point2D(currentPos.getX() - 1, currentPos.getY()),
                new Point2D(currentPos.getX(), currentPos.getY() + 1),
                new Point2D(currentPos.getX(), currentPos.getY() - 1)
        );

        for (Point2D move : possibleMoves) {
            int tileX = (int) move.getX();
            int tileY = (int) move.getY();
            boolean bloqueado = isBlocked(tileX, tileY);
            System.out.println("[ENEMY] ¿Casilla bloqueada? " + bloqueado);

            boolean libre = !POSITION_MAP.containsKey(move) &&
                    !isBlocked(tileX, tileY) &&  // 🔹 BLOQUEAMOS MOVIMIENTO SI HAY OBSTÁCULO
                    !turnManager.isTileOccupied(tileX, tileY);
            if (isBlocked(tileX, tileY)) {
                System.out.println("[ENEMY] Movimiento bloqueado: No puede avanzar a [" + tileX + ", " + tileY + "]");
                FXGL.getGameTimer().runOnceAfter(onTurnComplete, Duration.seconds(0.1));
                return; // 🔹 Detiene el movimiento si hay un obstáculo
            }
            if (bloqueado) {
                System.out.println("[ENEMY] Movimiento bloqueado: No puede avanzar a [" + tileX + ", " + tileY + "]");
                FXGL.getGameTimer().runOnceAfter(onTurnComplete, Duration.seconds(0.1));
                return; // 🔹 Detiene el movimiento si hay un obstáculo, enemigo o héroe
            }
            if (libre) {
                executeMoveSlowly(currentPos, move, onTurnComplete);
                return;
            }
        }

        // Si no puede moverse, enemigo espera su turno
        System.out.println("[ENEMY] Movimiento bloqueado por obstáculo en [" + logic.getX() + ", " + logic.getY() + "]");
        FXGL.getGameTimer().runOnceAfter(onTurnComplete, Duration.seconds(0.1));
    }

    private void patrol(Runnable onTurnComplete) {
        int randomX = logic.getX() + (random.nextBoolean() ? 1 : -1);
        int randomY = logic.getY() + (random.nextBoolean() ? 1 : -1);
        Point2D currentPos = new Point2D(logic.getX(), logic.getY());
        Point2D newPos = new Point2D(randomX, randomY);
        if (!POSITION_MAP.containsKey(newPos) && !isBlocked(randomX, randomY)) {
            executeMoveSlowly(currentPos, newPos, onTurnComplete);
        } else {
            System.out.println("[ENEMY] Movimiento bloqueado por obstáculo en [" + randomX + ", " + randomY + "]");
            FXGL.getGameTimer().runOnceAfter(onTurnComplete, Duration.seconds(0.1));
        }

    }

    private void executeMoveSlowly(Point2D oldPos, Point2D newPos, Runnable onTurnComplete) {
        POSITION_MAP.remove(oldPos);
        logic.setPosition((int) newPos.getX(), (int) newPos.getY());

        Point2D targetPosition = new Point2D(
                newPos.getX() * TILE_SIZE + visualOffsetX,
                newPos.getY() * TILE_SIZE + visualOffsetY
        );

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(enemyEntity.xProperty(), enemyEntity.getX()),
                        new KeyValue(enemyEntity.yProperty(), enemyEntity.getY())
                ),
                new KeyFrame(Duration.seconds(0.4),
                        new KeyValue(enemyEntity.xProperty(), targetPosition.getX(), Interpolator.EASE_OUT),
                        new KeyValue(enemyEntity.yProperty(), targetPosition.getY(), Interpolator.EASE_OUT)
                )
        );
        timeline.setOnFinished(e -> {
            POSITION_MAP.put(newPos, this);
            onTurnComplete.run();
        });
        timeline.play();
    }

    private boolean isBlocked(int tileX, int tileY) {
        System.out.println("Obstáculos detectados: " + FXGL.getGameWorld().getEntitiesByType(EntityType.OBSTACULOS).size());

        // Verificar obstáculos
        boolean blockedByObstacle = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.OBSTACULOS)
                .stream()
                .anyMatch(e -> {
                    int ex = (int) (Math.round(e.getX() / 56));
                    int ey = (int) (Math.round(e.getY() / 56));
                    boolean colision = ex == tileX && ey == tileY;
                    System.out.println("Obstáculo en [" + ex + ", " + ey + "] → Bloqueo solicitado [" + tileX + ", " + tileY + "] → " + colision);
                    return colision;
                });

        // Verificar enemigos
        boolean blockedByEnemy = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.ENEMY)
                .stream()
                .anyMatch(e -> {
                    int ex = (int) (Math.round(e.getX() / 56));
                    int ey = (int) (Math.round(e.getY() / 56));
                    boolean colision = ex == tileX && ey == tileY;
                    System.out.println("Enemigo en [" + ex + ", " + ey + "] → Bloqueo solicitado [" + tileX + ", " + tileY + "] → " + colision);
                    return colision;
                });

        // Verificar héroes
        boolean blockedByHero = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PLAYER)
                .stream()
                .anyMatch(e -> {
                    int ex = (int) (Math.round(e.getX() / 56));
                    int ey = (int) (Math.round(e.getY() / 56));
                    boolean colision = ex == tileX && ey == tileY;
                    System.out.println("Héroe en [" + ex + ", " + ey + "] → Bloqueo solicitado [" + tileX + ", " + tileY + "] → " + colision);
                    return colision;
                });

        // Bloquear si hay un obstáculo, enemigo o héroe en la casilla
        return blockedByObstacle || blockedByEnemy || blockedByHero;
    }




    public void showDamage(int amount) {
        Text damageText = new Text("-" + amount);
        damageText.setFill(Color.RED);
        damageText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        damageText.setStroke(Color.BLACK);
        damageText.setStrokeWidth(1);

        Point2D worldCenter = enemyEntity.getCenter();
        double cameraX = FXGL.getGameScene().getViewport().getX();
        double cameraY = FXGL.getGameScene().getViewport().getY();
        Point2D screenCenter = new Point2D(worldCenter.getX() - cameraX, worldCenter.getY() - cameraY);

        double textX = screenCenter.getX() - damageText.getLayoutBounds().getWidth() / 2;
        double textY = screenCenter.getY() - TILE_SIZE / 2;

        FXGL.getGameScene().addUINode(damageText);
        FXGL.animationBuilder()
                .duration(Duration.seconds(0.75))
                .onFinished(() -> FXGL.getGameScene().removeUINode(damageText))
                .translate(damageText)
                .from(new Point2D(textX, textY))
                .to(new Point2D(textX, textY - 40))
                .buildAndPlay();
    }

    public Enemy getLogic() {
        return logic;
    }
    public void setEntity(Entity entity) {
        this.enemyEntity = entity;
    }

    public Entity getEntity() {
        return enemyEntity;
    }

    public void onRemoved() {
        POSITION_MAP.remove(new Point2D(logic.getX(), logic.getY()));
    }

    private static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private Point2D findAvailablePosition(Point2D desiredPos) {
        if (!POSITION_MAP.containsKey(desiredPos) && !isBlocked((int) desiredPos.getX(), (int) desiredPos.getY())) {
            return desiredPos;
        }

        for (int radius = 1; radius <= 3; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    if (x == 0 && y == 0) continue;
                    Point2D candidate = new Point2D(desiredPos.getX() + x, desiredPos.getY() + y);
                    if (!POSITION_MAP.containsKey(candidate) && !isBlocked((int) candidate.getX(), (int) candidate.getY())) {
                        return candidate;
                    }
                }
            }
        }

        return desiredPos;
    }
    public boolean isTileOccupied(int tileX, int tileY) {
        return FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).stream()
                .anyMatch(e -> {
                    int ex = (int)(e.getX() / 56);
                    int ey = (int)(e.getY() / 56);
                    return ex == tileX && ey == tileY && e != this.getEntity();
                }) ||
                FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).stream()
                        .anyMatch(e -> {
                            int px = (int)(e.getX() / 56);
                            int py = (int)(e.getY() / 56);
                            return px == tileX && py == tileY;
                        });
    }

}
