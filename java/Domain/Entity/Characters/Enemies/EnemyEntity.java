package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Players.PlayerEntity;
import Domain.Entity.Combat.TurnManager;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;

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
    private double width = 56;  // ejemplo: ancho en píxeles
    private double height = 56; // ejemplo: alto en píxeles

    public EnemyEntity(Enemy logic, List<PlayerEntity> targets, TurnManager manager) {
        this.logic = logic;
        this.targets = new ArrayList<>(targets);
        this.turnManager = manager;
        assignVisualOffset();
    }

    public void initializeEntity(Entity entity) {
        this.enemyEntity = entity;

        // Posicionar en mapa de posiciones
        Point2D pos = new Point2D(logic.getX(), logic.getY());
        POSITION_MAP.put(pos, this);

        // Posicionar visualmente en el mundo con offset
        enemyEntity.setX(pos.getX() * TILE_SIZE + visualOffsetX);
        enemyEntity.setY(pos.getY() * TILE_SIZE + visualOffsetY);

        // LLAMA AL MÉTODO AQUÍ PARA ACTUALIZAR EL TAMAÑO
        updateSizeFromEntity();

        // Por ejemplo, imprime para ver que se actualizó
        System.out.println("[DEBUG] Enemy width: " + width + ", height: " + height);
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
    public void updateSizeFromEntity() {
        if (enemyEntity != null && enemyEntity.getViewComponent() != null) {
            if (!enemyEntity.getViewComponent().getChildren().isEmpty()) {
                Node view = enemyEntity.getViewComponent().getChildren().get(0);
                width = view.getBoundsInLocal().getWidth();
                height = view.getBoundsInLocal().getHeight();
            }
        }
    }


    public boolean canMoveTo(int nextTileX, int nextTileY) {
        double newX = nextTileX * TILE_SIZE;
        double newY = nextTileY * TILE_SIZE;
        Rectangle2D futureBB = new Rectangle2D(newX, newY, width, height);

        // Verificar colisión con obstáculos
        boolean blockedByObstacles = FXGL.getGameWorld().getEntitiesByType(EntityType.OBSTACULOS)
                .stream()
                .anyMatch(o -> {
                    Rectangle2D obBB = new Rectangle2D(o.getX(), o.getY(), o.getWidth(), o.getHeight());
                    return futureBB.intersects(obBB);
                });
        if (blockedByObstacles) return false;

        // Verificar colisión con jugadores
        boolean blockedByPlayers = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER)
                .stream()
                .anyMatch(p -> {
                    Rectangle2D playerBB = new Rectangle2D(p.getX(), p.getY(), p.getWidth(), p.getHeight());
                    return futureBB.intersects(playerBB);
                });
        if (blockedByPlayers) return false;

        // Verificar colisión con otros enemigos
        boolean blockedByEnemies = FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY)
                .stream()
                .anyMatch(e -> {
                    // Ignorar colisión con uno mismo si es el mismo enemigo
                    if (e == this.enemyEntity) return false;

                    Rectangle2D enemyBB = new Rectangle2D(e.getX(), e.getY(), e.getWidth(), e.getHeight());
                    return futureBB.intersects(enemyBB);
                });
        if (blockedByEnemies) return false;

        return true;
    }
    private void moveTowardPlayer(PlayerEntity target, Runnable onTurnComplete) {
        Point2D currentPos = new Point2D(logic.getX(), logic.getY());
        Point2D targetPos = new Point2D(target.getHero().getX(), target.getHero().getY());

        // Buscar tiles libres en un rango de 2 alrededor del actual
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (dx == 0 && dy == 0) continue;  // no quedarse en el mismo lugar

                Point2D move = new Point2D(currentPos.getX() + dx, currentPos.getY() + dy);
                int tileX = (int) move.getX();
                int tileY = (int) move.getY();
                System.out.println("[DEBUG turnManager] Tile (" + tileX + ", " + tileY + ") ocupado? " + turnManager.isTileOccupied(tileX, tileY));

                // Debug
                if (!posicionLibre(move)) {
                    System.out.println("[DEBUG] posicionLibre: tile (" + tileX + ", " + tileY + ") está ocupado.");
                } else {
                    System.out.println("[DEBUG] Tile (" + tileX + ", " + tileY + ") está libre.");
                }

                if (!isBlocked(tileX, tileY) && !POSITION_MAP.containsKey(move) && !turnManager.isTileOccupied(tileX, tileY) && posicionLibre(move)) {
                    executeMoveSlowly(currentPos, move, onTurnComplete);
                    return;
                }
            }
        }

        System.out.println("[ENEMY] No puede moverse hacia el jugador, esperando turno.");
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
        POSITION_MAP.put(newPos, this);
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
            enemyEntity.getComponent(PhysicsComponent.class).overwritePosition(targetPosition);
            POSITION_MAP.put(newPos, this);

            // Centrar cámara en enemigo con offsets 0, 0
            FXGL.getGameScene().getViewport().bindToEntity(enemyEntity, 0, 0);

            onTurnComplete.run();
        });


        timeline.play();
    }

    private boolean isBlocked(int tileX, int tileY) {
        System.out.println("[DEBUG isBlocked] Chequeando tile (" + tileX + ", " + tileY + ")");

        boolean blockedByObstacle = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.OBSTACULOS)
                .stream()
                .anyMatch(e -> {
                    int ex = (int) (Math.round(e.getX() / TILE_SIZE));
                    int ey = (int) (Math.round(e.getY() / TILE_SIZE));
                    boolean collision = ex == tileX && ey == tileY;
                    if (collision)
                        System.out.println("[DEBUG isBlocked] Bloqueado por OBSTÁCULO en (" + ex + ", " + ey + ")");
                    return collision;
                });

        boolean blockedByEnemy = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.ENEMY)
                .stream()
                .anyMatch(e -> {
                    int ex = (int) (Math.round(e.getX() / TILE_SIZE));
                    int ey = (int) (Math.round(e.getY() / TILE_SIZE));
                    boolean collision = ex == tileX && ey == tileY;
                    if (collision)
                        System.out.println("[DEBUG isBlocked] Bloqueado por ENEMIGO en (" + ex + ", " + ey + ")");
                    return collision;
                });

        boolean blockedByHero = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PLAYER)
                .stream()
                .anyMatch(e -> {
                    int ex = (int) (Math.round(e.getX() / TILE_SIZE));
                    int ey = (int) (Math.round(e.getY() / TILE_SIZE));
                    boolean collision = ex == tileX && ey == tileY;
                    if (collision)
                        System.out.println("[DEBUG isBlocked] Bloqueado por HÉROE en (" + ex + ", " + ey + ")");
                    return collision;
                });

        return blockedByObstacle || blockedByEnemy || blockedByHero;
    }


    private boolean posicionLibre(Point2D tilePos) {
        int tileX = (int) tilePos.getX();
        int tileY = (int) tilePos.getY();

        long count = FXGL.getGameWorld().getEntities().stream()
                .filter(e -> e.getComponentOptional(CollidableComponent.class).isPresent())
                .filter(e -> {
                    int ex = (int) (e.getX() / TILE_SIZE);
                    int ey = (int) (e.getY() / TILE_SIZE);
                    boolean collision = ex == tileX && ey == tileY;
                    if (collision)
                        System.out.println("[DEBUG posicionLibre] Colisionable en (" + ex + ", " + ey + ")");
                    return collision;
                }).count();

        System.out.println("[DEBUG posicionLibre] Colisionables en tile (" + tileX + ", " + tileY + "): " + count);
        return count == 0;
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
                    int ex = (int) (e.getX() / TILE_SIZE);
                    int ey = (int) (e.getY() / TILE_SIZE);
                    return ex == tileX && ey == tileY && e != this.enemyEntity;
                }) ||
                FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).stream()
                        .anyMatch(e -> {
                            int px = (int) (e.getX() / TILE_SIZE);
                            int py = (int) (e.getY() / TILE_SIZE);
                            return px == tileX && py == tileY;
                        });
    }


}

