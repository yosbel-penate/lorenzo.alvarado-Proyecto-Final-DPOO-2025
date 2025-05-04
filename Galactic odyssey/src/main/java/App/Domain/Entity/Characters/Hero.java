package Domain.Entity.Characters;

import Domain.Tile.Tile; // Se asume que "Casilla" se ha renombrado a "Tile"
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Rectangle2D;
import Domain.Entity.EntityType;
import java.util.List;
import java.util.Random;

public abstract class Hero {
    // Basic attributes
    protected String name = "Hero";
    protected int health = 100;
    protected int takeDamage; // This may be used for individual damage calculations
    protected final int TILE_SIZE = 32;
    protected Entity playerEntity;

    // Combat attributes: Each hero assigns its attack individually; defense starts at 0.
    protected int attack;       // To be assigned in each subclass as needed.
    protected int defense = 0;  // All heroes start with 0 defense.
    protected String spritePath;  // Each character will have its own image

    protected Random random = new Random();

    /**
     * Spawns the hero by building and attaching its entity.
     */
    public void spawn() {
        playerEntity = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(287, 448) // Default initial position
                .view(FXGL.texture(spritePath, 32, 64))  // Uses the specific image for each character
                .bbox(new HitBox("BODY", BoundingShape.box(32, 64)))
                .buildAndAttach();

        FXGL.getGameScene().getViewport()
                .bindToEntity(playerEntity, FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);
    }

    /**
     * Moves the hero within the grid (in tiles).
     * @param dx Number of tiles to move in the x direction.
     * @param dy Number of tiles to move in the y direction.
     */
    public void moveByGrid(int dx, int dy) {
        double newX = playerEntity.getX() + dx * TILE_SIZE;
        double newY = playerEntity.getY() + dy * TILE_SIZE;
        double playerWidth = playerEntity.getWidth();
        double playerHeight = playerEntity.getHeight();
        Rectangle2D futureBoundingBox = new Rectangle2D(newX, newY, playerWidth, playerHeight);

        // Here you can incorporate collision checks, etc.
        playerEntity.setPosition(newX, newY);
    }

    // Convenience methods for movement
    public void moveRight() { moveByGrid(1, 0); }
    public void moveLeft()  { moveByGrid(-1, 0); }
    public void moveUp()    { moveByGrid(0, -1); }
    public void moveDown()  { moveByGrid(0, 1); }

    /**
     * Returns the visual entity.
     * @return The hero's entity.
     */
    public Entity getEntity() {
        return playerEntity;
    }

    /**
     * Returns the grid position (in tiles) of the hero.
     * @return A Tile representing the current hero's position.
     */
    public Tile getPositionHero() {
        int tileX = (int) (playerEntity.getX() / TILE_SIZE);
        int tileY = (int) (playerEntity.getY() / TILE_SIZE);
        return new Tile(tileX, tileY);
    }

    // Getters for name, attack, and defense.
    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    /**
     * Buffs the hero's stats by increasing attack and defense.
     * @param bonusAttack The additional attack points.
     * @param bonusDefense The additional defense points.
     */
    public void buffStats(int bonusAttack, int bonusDefense) {
        this.attack += bonusAttack;
        this.defense += bonusDefense;
    }

    /**
     * Receives damage, subtracts it from health, and prints a message.
     * If health falls to zero or below, indicates that the hero is defeated.
     *
     * @param damage The damage amount.
     */
    public void takeDamage(int damage) {
        health -= damage;
        System.out.println(name + " takes " + damage + " damage. Current health: " + health);
        if (health <= 0) {
            System.out.println(name + " has been defeated.");
            // Here you may implement logic to remove or restart the hero.
        }
    }

    public int getHealth() {
        return health;
    }

    public int getTakeDamage() {
        return takeDamage;
    }

    /**
     * Starts the hero's turn. Resets any necessary turn-based counters and activates the special ability.
     * @param allies A list of allied heroes.
     */
    public void startTurn(List<Hero> allies) {
        // Here you could reset counters or other turn-based values.
        specialAbility(allies);
    }

    /**
     * Abstract method to define the hero's special ability.
     * @param allies A list of allied heroes.
     */
    protected abstract void specialAbility(List<Hero> allies);
}
