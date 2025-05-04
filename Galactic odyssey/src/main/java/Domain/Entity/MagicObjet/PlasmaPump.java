package Domain.MagicObjet;

import Domain.Entity.Characters.Hero;
import Domain.Entity.enemies.Enemy; // Updated from plural "Enemies" to singular "Enemy"
import Domain.Tile.Tile; // Previously Casilla
import java.util.List;
import java.util.Random;

public class PlasmaPump extends MagicObject {

    // Variables for position in tiles
    private int plasmaPumpX;
    private int plasmaPumpY;

    // Counter for plasma pumps
    private int plasmaPumpCounter = 0;

    // Random number generator instance
    private Random random = new Random();

    public PlasmaPump(String name, int usageRange) {
        super();
        // Optionally, initialize fields using name and usageRange if needed.
    }

    /**
     * Simulates the hero picking up the plasma pump.
     *
     * @param hero the hero acquiring the pump.
     */
    public void acquirePlasmaPump(Hero hero) {
        // Assume MagicObject is a collection (static or otherwise) used to track possession.
        if (!MagicObject.contains("PlasmaPump")) {
            MagicObject.add("PlasmaPump");
        }
        plasmaPumpCounter++;
        System.out.println("Obtained a Plasma Bomb");
    }

    /**
     * Updates the pump's position based on the hero's current tile.
     *
     * @param hero the hero whose position is used.
     */
    public void updatePumpPosition(Hero hero) {
        Tile heroTile = hero.getPositionHero();
        plasmaPumpX = heroTile.getX();
        plasmaPumpY = heroTile.getY();
    }

    /**
     * Sets the pump's X position using the position of the first hero in the list.
     *
     * @param heroes the list of heroes.
     */
    public void setPlasmaPumpX(List<Hero> heroes) {
        if (!heroes.isEmpty()) {
            plasmaPumpX = heroes.get(0).getPositionHero().getX();
        }
    }

    /**
     * Sets the pump's Y position using the position of the first hero in the list.
     *
     * @param heroes the list of heroes.
     */
    public void setPlasmaPumpY(List<Hero> heroes) {
        if (!heroes.isEmpty()) {
            plasmaPumpY = heroes.get(0).getPositionHero().getY();
        }
    }

    public int getPlasmaPumpX() {
        return plasmaPumpX;
    }

    public int getPlasmaPumpY() {
        return plasmaPumpY;
    }

    /**
     * Activates the plasma pump. Calculates the explosion range and, if the enemy is within the impact area, applies damage.
     *
     * @param enemy  the enemy to target.
     * @param heroes the list of heroes.
     * @param tile   a tile instance used for grid-based distance calculations.
     */
    public void activatePlasmaPump(Enemy enemy, List<Hero> heroes, Tile tile) {
        // Obtain distance lists using the tile's methods (assume these methods exist):
        // - calculateManhattanDistances(enemy, heroes)
        // - calculateXDistances(enemy, heroes)
        // - calculateYDistances(enemy, heroes)
        List<Integer> distanceList = tile.calculateManhattanDistances(enemy, heroes);
        List<Integer> distanceXList = tile.calculateXDistances(enemy, heroes);
        List<Integer> distanceYList = tile.calculateYDistances(enemy, heroes);

        // Calculate the distance between the pump and the enemy on each axis and in total
        int enemyDistanceX = Math.abs(plasmaPumpX - enemy.getPositionEnemies().getX());
        int enemyDistanceY = Math.abs(plasmaPumpY - enemy.getPositionEnemies().getY());
        int enemyDistance = enemyDistanceX + enemyDistanceY;

        // Iterate through the obtained distances
        for (int compareDist : distanceList) {
            if (compareDist <= 8) { // Explosion range of 8 (including impact radius)
                while (compareDist <= 5) { // While the object is within 5 tiles or less
                    for (int compareX : distanceXList) { // Check movement range on the X-axis
                        if (compareX != 0) {
                            plasmaPumpY++;
                            break;
                        }
                        for (int compareY : distanceYList) { // Check movement range on the Y-axis
                            if (compareY != 0) {
                                plasmaPumpX++;
                                break;
                            } else {
                                System.out.println("Invalid movement range, do not use");
                            }
                        }
                        int randomNumber = random.nextInt(10); // Generate a random number from 0 to 9
                        if (enemyDistance <= 3) {
                            // Apply damage to the enemy based on the randomly generated number
                            if (randomNumber < 3) {
                                enemy.takeDamage(4);
                            } else if (randomNumber < 7) {
                                enemy.takeDamage(5);
                            } else {
                                enemy.takeDamage(6);
                            }
                            MagicObject.remove("PlasmaPump");
                            plasmaPumpCounter = 0;
                        }
                    }
                    break; // Exit the while loop to avoid an infinite loop
                }
            }
        }
    }

    public String getPlasmaPump() {
        return "Number of Plasma Bombs: " + plasmaPumpCounter;
    }

    public int getPlasmaPumpInt() {
        return plasmaPumpCounter;
    }
}
