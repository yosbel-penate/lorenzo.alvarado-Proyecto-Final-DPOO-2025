package Domain.Entity.enemies;

import Domain.Entity.Characters.Hero;
import Domain.Entity.EntityType;
import Domain.Tile.Tile; // Previously: Domain.casilla.Casilla
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.application.Platform;

import java.util.*;
import java.util.function.Function;

public abstract class Enemy {

    // Fields for position (in tiles) and movement
    private Tile position;
    private List<Tile> obstacles;
    protected Hero hero;
    private Random random = new Random();
    private EnemyController controller;
    // Fields for enemy damage logic and names
    protected int health;
    protected String name;
    protected int rangeDamage;
    protected String specialAbility;
    protected int velocity;
    protected Entity entity;
    protected String spritePath;
    int attackRange;

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Constructor.
     *
     * @param name           The enemy name.
     * @param rangeDamage    The attack range.
     * @param specialAbility The special ability name.
     * @param velocity       The movement velocity.
     * @param health         The initial health.
     * @param x              The starting X position (in tiles).
     * @param y              The starting Y position (in tiles).
     * @param spritePath     The path to the enemy's sprite.
     */
    public Enemy(String name, int rangeDamage, String specialAbility, int velocity, int health, int x, int y, String spritePath, int attackRange) {
        this.position = new Tile(x, y);
        this.name = name;
        this.rangeDamage = rangeDamage;
        this.specialAbility = specialAbility;
        this.velocity = velocity;
        this.health = health;
        this.spritePath = spritePath; // Assigns the specific image for the enemy.
        this.attackRange = attackRange;
    }

    public int getHealth() {
        return health;
    }

    public void setController(EnemyController controller) {
        this.controller = controller;
    }

    public EnemyController getController() {
        return controller;
    }

    public Tile getPositionEnemies() {
        return position;
    }

    public void setPosition(Tile newPosition) {
        if (newPosition != null) {
            this.position = newPosition;
        }
    }

    // Movement and pursuit methods based on grid (Tile)
    public void updateMovement(Hero hero, List<Tile> obstacles) {
        if (hero != null && hero.getPositionHero() != null && shouldPursue(hero)) {
            pursueHero(hero, obstacles);
        } else {
            moveRandomly(obstacles, position);
        }
    }

    private boolean shouldPursue(Hero hero) {
        return position.distanceTo(hero.getPositionHero()) < 10;
    }

    public void pursueHero(Hero hero, List<Tile> obstacles) {
        Tile destination = hero.getPositionHero();
        Tile.Direction direction = position.directionTo(destination);
        if (direction == Tile.Direction.NONE) {
            moveRandomly(obstacles, destination);
        } else {
            moveIfSafe(direction, obstacles, destination);
        }
    }

    public void moveRandomly(List<Tile> obstacles, Tile heroPosition) {
        List<Tile.Direction> safeDirections = getSafeDirections(obstacles, heroPosition);
        if (!safeDirections.isEmpty()) {
            Tile.Direction randomDirection = safeDirections.get(random.nextInt(safeDirections.size()));
            moveIfSafe(randomDirection, obstacles, heroPosition);
        } else {
            System.out.println("No safe moves available from " + position);
        }
    }

    private void moveIfSafe(Tile.Direction direction, List<Tile> obstacles, Tile heroPosition) {
        int newX = position.getX();
        int newY = position.getY();

        // Adjust the position based on the direction.
        switch (direction) {
            case RIGHT -> newX++;
            case LEFT -> newX--;
            case UP -> newY--;
            case DOWN -> newY++;
            case NONE -> {
                return;
            }
        }

        Tile newPosition = new Tile(newX, newY);

        // Check that the new tile is not blocked by an obstacle and is not the hero's tile.
        if (!isBlocked(newPosition, obstacles) && !newPosition.equals(heroPosition)) {
            setPosition(newPosition);
        } else {
            // In case of collision, try alternative safe directions.
            List<Tile.Direction> alternatives = getSafeDirections(obstacles, heroPosition);
            if (!alternatives.isEmpty()) {
                Tile.Direction fallback = alternatives.get(random.nextInt(alternatives.size()));
                int fbX = position.getX();
                int fbY = position.getY();
                switch (fallback) {
                    case RIGHT -> fbX++;
                    case LEFT -> fbX--;
                    case UP -> fbY--;
                    case DOWN -> fbY++;
                    default -> {
                    }
                }
                Tile fallbackPos = new Tile(fbX, fbY);
                if (!isBlocked(fallbackPos, obstacles) && !fallbackPos.equals(heroPosition)) {
                    setPosition(fallbackPos);
                } else {
                    System.out.println("Alternative move also blocked at " + fallbackPos);
                }
            } else {
                System.out.println("No safe alternative moves from " + position);
            }
        }
    }

    private List<Tile.Direction> getSafeDirections(List<Tile> obstacles, Tile heroPosition) {
        List<Tile.Direction> safe = new ArrayList<>();
        for (Tile.Direction dir : Tile.Direction.values()) {
            if (dir == Tile.Direction.NONE)
                continue;
            int nx = position.getX();
            int ny = position.getY();
            switch (dir) {
                case RIGHT -> nx++;
                case LEFT -> nx--;
                case UP -> ny--;
                case DOWN -> ny++;
            }
            Tile possible = new Tile(nx, ny);
            if (!isBlocked(possible, obstacles) && !possible.equals(heroPosition)) {
                safe.add(dir);
            }
        }
        return safe;
    }

    private boolean isBlocked(Tile newPosition, List<Tile> obstacles) {
        if (obstacles == null)
            return false;
        return obstacles.stream().anyMatch(obstacle -> obstacle.equals(newPosition));
    }

    /**
     * Applies damage to the enemy.
     * Subtracts the given damage from health and outputs messages;
     * if health reaches zero or below, it indicates that the enemy has been defeated.
     */
    public void takeDamage(int damage) {
        health -= damage;
        System.out.println(name + " receives " + damage + " damage. Current health: " + health);
        if (health <= 0) {
            System.out.println(name + " has been defeated.");
            // Remove the FXGL entity from the world.
            entity.removeFromWorld();
        }
    }

    private double calculateDistance(Tile enemyPos, Tile heroPos) {
        if (heroPos == null) {
            System.out.println("Error: Hero's position is null.");
            return 0;
        }
        int dx = enemyPos.getX() - heroPos.getX();
        int dy = enemyPos.getY() - heroPos.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void enemyTurn(List<Tile> obstacles) {
        this.obstacles = obstacles;
        System.out.println(name + " is taking its turn...");

        // Attack logic: check if the hero is in range.
        double distance = position.distanceTo(hero.getPositionHero());
        if (distance <= rangeDamage) {
            int damage = random.nextInt(3) + 2; // Random damage between 2 and 4.
            System.out.println(name + " attacks " + hero.getName() + " with " + damage + " damage.");
            hero.takeDamage(damage);
        } else {
            // If not in range, move strategically.
            updateMovement(hero, obstacles);
        }
    }

    public String getSpritePath() {
        return spritePath;
    }

    // REGISTRY: Mapping between enemy type identifier and its constructor function.
    private static Map<String, Function<SpawnData, Enemy>> enemyRegistry = new HashMap<>();

    public static void registerEnemy(String key, Function<SpawnData, Enemy> enemyConstructor) {
        enemyRegistry.put(key, enemyConstructor);
    }

    public static Enemy createEnemy(String key, SpawnData data) {
        if (!enemyRegistry.containsKey(key)) {
            throw new IllegalArgumentException("Unknown enemy type: " + key);
        }
        return enemyRegistry.get(key).apply(data);
    }

    @Spawns("enemy")
    public void spawn(Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("The hero parameter cannot be null.");
        }
        this.hero = hero;

        Tile pos = getPositionEnemies();
        if (pos == null) {
            throw new IllegalStateException("The enemy's position cannot be null.");
        }

        SpawnData data = new SpawnData(pos.getX(), pos.getY())
                .put("type", "enemy")
                .put("hero", hero);

        Entity entity = FXGL.entityBuilder(data)
                .type(EntityType.ENEMY)
                .viewWithBBox(FXGL.texture(getSpritePath(), 48, 48))
                .buildAndAttach();

        setEntity(entity);

        // Instantiate and assign the enemy controller
        EnemyController controller = new EnemyController(this, hero);
        setController(controller);
        entity.setProperty("controller", controller);
    }

    /**
     * Removes the enemy's entity from the FXGL world.
     * Checks that the entity is not null before calling removeEntity.
     */
    public void removeFromWorld() {
        if (entity != null) {
            Platform.runLater(() -> {
                FXGL.getGameWorld().removeEntity(entity);
                System.out.println(name + " has been removed from the world.");
            });
            entity = null; // Clear the reference
        } else {
            System.out.println("Cannot remove " + name + ", the entity is null.");
        }
    }

}