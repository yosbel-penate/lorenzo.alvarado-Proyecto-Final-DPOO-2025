package Domain.Entity.enemies;

import Domain.Entity.Characters.Hero;
import Domain.Tile.Tile; // Antes: Domain.casilla.Casilla
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CombatDrone extends Enemy {

    // Fields specific to CombatDrone
    private final Random random = new Random();
    private int velocity;
    private String specialAbility;
    private List<Hero> heroesInGame;
    private Entity enemyEntity; // FXGL entity representing the drone
    private static int counter = 0;

    public CombatDrone() {
        super("CombatDrone" + (++counter), 3, "Scanning", 1, 12, 1536, 320, "combatDrone.png",3);
        // The enemy's specific image is assigned.
        // 'attackRange' (inherited from Enemy) is set to 3.
        // 'velocity' and 'specialAbility' can be adjusted as needed.
        this.velocity = 1;
        this.specialAbility = "Scanning";
    }

    /**
     * Helper method that calculates the Manhattan distance between two grid points.
     */
    private int calculateManhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /**
     * Returns the first hero that is within attack range (using Manhattan distance).
     *
     * @param heroes A list of heroes to check.
     * @return An Optional containing the first hero in range, or empty if none are in range.
     */
    public Optional<Hero> getHeroInAttackRange(List<Hero> heroes) {
        return heroes.stream().filter(hero -> {
            Tile heroPos = hero.getPositionHero();

            return calculateManhattanDistance(
                    heroPos.getX(),
                    heroPos.getY(),
                    getPositionEnemies().getX(),
                    getPositionEnemies().getY()
            ) <= attackRange;
        }).findFirst();
    }

    /**
     * Executes an attack on the hero within attack range.
     *
     * @param heroes The list of heroes to target.
     */
    public void executeAttack(List<Hero> heroes) {
        Optional<Hero> target = getHeroInAttackRange(heroes);
        target.ifPresent(hero -> {
            Tile heroPos = hero.getPositionHero();
            int distance = calculateManhattanDistance(
                    heroPos.getX(),
                    heroPos.getY(),
                    getPositionEnemies().getX(),
                    getPositionEnemies().getY()
            );
            if (distance <= attackRange) {
                int damage = 3 + random.nextInt(3); // Random damage between 3 and 5
                System.out.println(name + " attacks " + hero.getName() + " with " + damage + " damage.");
                hero.takeDamage(damage);
            }
        });
    }

    /**
     * Executes the special ability: the drone moves closer to heroes that are 5 tiles or less away.
     * The movement is applied independently on each axis (X and Y).
     */
    public void executeSpecialAbility() {
        for (Hero hero : heroesInGame) {
            Tile heroPos = hero.getPositionHero();
            int distance = calculateManhattanDistance(
                    heroPos.getX(),
                    heroPos.getY(),
                    getPositionEnemies().getX(),
                    getPositionEnemies().getY()
            );
            if (distance <= 5) {
                // Obtain current grid position of the drone.
                int currentX = getPositionEnemies().getX();
                int currentY = getPositionEnemies().getY();
                if (heroPos.getX() > currentX) {
                    currentX += velocity;
                } else if (heroPos.getX() < currentX) {
                    currentX -= velocity;
                }
                if (heroPos.getY() > currentY) {
                    currentY += velocity;
                } else if (heroPos.getY() < currentY) {
                    currentY -= velocity;
                }
                setPosition(new Tile(currentX, currentY));
                System.out.println(name + " approaches " + hero.getName() + " using " + specialAbility);
            }
        }
    }

    /**
     * Spawns the enemy using FXGL and assigns the image "combatDrone.png".
     * Ensure the image exists in the resources folder and adjust its size if necessary.
     */
    public void spawnEnemy() {
        enemyEntity = FXGL.entityBuilder()
                .at(getPositionEnemies().getX(), getPositionEnemies().getY())
                .view(FXGL.texture("combatDrone.png", 48, 48))
                .buildAndAttach();
    }
}
