package Domain.Entity.Characters.Enemies;

import Domain.Entity.Characters.Players.PlayerEntity;
import Domain.Entity.Combat.TurnManager;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.*;

public class EnemyEntity {
    private Entity enemyEntity;
    private DroneCombate logic;
    private List<PlayerEntity> targets;
    private final int TILE_SIZE = 56;
    private TurnManager turnManager;
    private Random random = new Random();
    private double visualOffsetX;
    private double visualOffsetY;
    private static final double MAX_OFFSET = 8.0;
    private static final Map<Point2D, EnemyEntity> POSITION_MAP = new HashMap<>();

    public EnemyEntity(List<PlayerEntity> targets, TurnManager manager) {
        this.logic = new DroneCombate();
        this.targets = new ArrayList<>(targets);
        this.turnManager = manager;
        assignVisualOffset();
    }

    private void assignVisualOffset() {
        this.visualOffsetX = (random.nextDouble() - 0.5) * MAX_OFFSET;
        this.visualOffsetY = (random.nextDouble() - 0.5) * MAX_OFFSET;
    }

    public void spawnEnemy(int x, int y) {
        Point2D desiredPos = new Point2D(x, y);
        Point2D finalPos = findAvailablePosition(desiredPos);

        logic.setPosition((int) finalPos.getX(), (int) finalPos.getY());
        logic.activate();

        enemyEntity = FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(finalPos.getX() * TILE_SIZE + visualOffsetX, finalPos.getY() * TILE_SIZE + visualOffsetY)
                .viewWithBBox(FXGL.texture("drone.png", TILE_SIZE, TILE_SIZE))
                .with(new CollidableComponent(true))
                .zIndex((int) (finalPos.getY() * TILE_SIZE + visualOffsetY))
                .buildAndAttach();

        POSITION_MAP.put(finalPos, this);
        TurnManager.ENEMY_LOGIC_MAP.put(enemyEntity, this);
    }

    public void takeTurn() {
        if (!logic.isAlive()) {
            turnManager.endTurn();
            return;
        }

        PlayerEntity target = findClosestAliveTarget();
        if (target == null) {
            turnManager.endTurn();
            return;
        }

        int distance = logic.getDistanceTo(target.getHero().getX(), target.getHero().getY());

        if (distance <= logic.attackRange) {
            attackPlayer(target);
        } else if (distance <= 10) {
            moveTowardPlayer(target);
        } else {
            patrol();
        }

        // Asegura terminar el turno tras ejecutar cualquier acción
        FXGL.getGameTimer().runOnceAfter(turnManager::endTurn, Duration.seconds(0.2));
    }

    private PlayerEntity findClosestAliveTarget() {
        return targets.stream()
                .filter(p -> p.getHero().isAlive())
                .min(Comparator.comparingInt(p -> logic.getDistanceTo(p.getHero().getX(), p.getHero().getY())))
                .orElse(null);
    }

    private void attackPlayer(PlayerEntity target) {
        int damage = random(logic.minDamage, logic.maxDamage);
        target.getHero().takeDamage(damage);
        showDamage(damage);
    }

    private void moveTowardPlayer(PlayerEntity target) {
        Point2D currentPos = new Point2D(logic.getX(), logic.getY());
        Point2D targetPos = new Point2D(target.getHero().getX(), target.getHero().getY());

        List<Point2D> possibleMoves = Arrays.asList(
                new Point2D(currentPos.getX() + 1, currentPos.getY()),
                new Point2D(currentPos.getX() - 1, currentPos.getY()),
                new Point2D(currentPos.getX(), currentPos.getY() + 1),
                new Point2D(currentPos.getX(), currentPos.getY() - 1)
        );

        for (Point2D move : possibleMoves) {
            if (!POSITION_MAP.containsKey(move) && !isBlocked((int) move.getX(), (int) move.getY())) {
                executeMove(currentPos, move);
                return;
            }
        }
    }

    private void patrol() {
        int randomX = logic.getX() + (random.nextBoolean() ? 1 : -1);
        int randomY = logic.getY() + (random.nextBoolean() ? 1 : -1);
        Point2D currentPos = new Point2D(logic.getX(), logic.getY());
        Point2D newPos = new Point2D(randomX, randomY);

        if (!POSITION_MAP.containsKey(newPos) && !isBlocked(randomX, randomY)) {
            executeMove(currentPos, newPos);
        }
    }

    private void executeMove(Point2D oldPos, Point2D newPos) {
        POSITION_MAP.remove(oldPos);
        logic.setPosition((int) newPos.getX(), (int) newPos.getY());
        enemyEntity.setPosition(newPos.getX() * TILE_SIZE + visualOffsetX, newPos.getY() * TILE_SIZE + visualOffsetY);
        POSITION_MAP.put(newPos, this);
    }

    private boolean isBlocked(int x, int y) {
        return FXGL.getGameWorld()
                .getEntitiesByType(EntityType.OBSTACULOS)
                .stream()
                .anyMatch(e -> e.getX() == x * TILE_SIZE && e.getY() == y * TILE_SIZE);
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

    public DroneCombate getLogic() {
        return logic;
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
}
