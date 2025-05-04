package Domain.Entity.Characters;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import Domain.Entity.EntityType;
import java.util.List;
import Domain.Tile.Tile;  // Antes era Domain.casilla.Casilla

public class CaptainOrion extends Hero {

    // Movement limit per turn for Captain Orion: 5 tiles.
    private final int movementLimit = 5;
    private int remainingMovement = movementLimit;

    // Special ability cooldown: can be activated every 3 turns.
    private final int specialCooldown = 3;
    private int specialCooldownCounter = 0; // 0 means the ability is available.

    // Constructor: assigns specific values.
    public CaptainOrion() {
        this.name = "Captain Orion";
        this.health = 20;
        this.spritePath = "personaje.png";
        // Initial health is 20.
        // This attack method uses random damage, so no fixed "attack" value is assigned.
        // Defense starts at 0.
    }

    /**
     * Overrides spawn() to use a unique image and starting position for Captain Orion.
     */
    @Override
    public void spawn() {
        playerEntity = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(287, 448)  // Starting position (can be adjusted).
                .view(FXGL.texture("personaje.png", 32, 64))  // Ensure this image exists.
                .bbox(new HitBox("BODY", BoundingShape.box(32, 64)))
                .buildAndAttach();

        FXGL.getGameScene().getViewport()
                .bindToEntity(playerEntity, FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);
    }

    /**
     * Overrides moveByGrid() to limit the number of tiles that can be moved per turn.
     * @param dx Number of tiles to move in the x direction.
     * @param dy Number of tiles to move in the y direction.
     */
    @Override
    public void moveByGrid(int dx, int dy) {
        if (remainingMovement > 0) {
            super.moveByGrid(dx, dy);
            remainingMovement--;
            System.out.println(name + " moves. Remaining moves this turn: " + remainingMovement);
        } else {
            System.out.println(name + ": No moves left for this turn.");
        }
    }

    /**
     * Special ability: Ally Buff.
     * Increases the attack and defense of other allied heroes by 3 points each.
     * Can only be activated if the cooldown is 0; after activation, the cooldown is reset to 3 turns.
     *
     * @param allies List of allied heroes.
     */
    @Override
    protected void specialAbility(List<Hero> allies) {
        if (specialCooldownCounter == 0) {
            int bonusAttack = 3;
            int bonusDefense = 3;
            System.out.println(name + " activates its special ability: Ally Buff!");
            for (Hero ally : allies) {
                if (ally != this) { // Do not buff itself.
                    ally.buffStats(bonusAttack, bonusDefense);
                    System.out.println(ally.getName() + " now has " + ally.getAttack() +
                            " attack and " + ally.getDefense() + " defense.");
                }
            }
            // Reset the cooldown.
            specialCooldownCounter = specialCooldown;
        } else {
            System.out.println("Special ability not available. Wait " + specialCooldownCounter + " turn(s).");
        }
    }

    /**
     * Prepares a new turn by resetting the movement and decrementing the cooldown.
     */
    public void newTurn() {
        remainingMovement = movementLimit;
        if (specialCooldownCounter > 0) {
            specialCooldownCounter--;
        }
        System.out.println(name + ": New turn. Moves: " + remainingMovement +
                ". Special ability available in: " + specialCooldownCounter + " turn(s).");
    }

    /**
     * Calculates the Manhattan distance (in tiles) between two tiles.
     * @param a The first tile.
     * @param b The second tile.
     * @return The Manhattan distance.
     */
    private int getManhattanDistance(Tile a, Tile b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    /**
     * Performs an attack on another hero only if the target is within a range of 3 tiles.
     * The damage is determined randomly between 2 and 4.
     *
     * @param target The target hero.
     */
    public void performAttack(Hero target) {
        Tile posThis = this.getPositionHero();
        Tile posTarget = target.getPositionHero();
        int distance = getManhattanDistance(posThis, posTarget);
        if (distance <= 3) {
            int damage = random.nextInt(3) + 2;  // Random damage between 2 and 4.
            System.out.println(name + " attacks " + target.getName() + " inflicting " + damage + " damage.");
            target.takeDamage(damage);
        } else {
            System.out.println("Target " + target.getName() + " is out of range (distance: " + distance + " tiles).");
        }
    }
}
