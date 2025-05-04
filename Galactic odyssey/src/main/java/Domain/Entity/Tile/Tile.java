package Domain.Tile;

import Domain.Entity.Characters.Hero;
import Domain.Entity.enemies.Enemy;
import java.util.ArrayList;
import java.util.List;

public class Tile {

    private int x;
    private int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Calculates the absolute distances in the X-axis between the enemy's tile and each hero's tile.
     *
     * @param enemy the enemy whose position is used
     * @param heroes list of heroes
     * @return a list of x-axis distances
     */
    public List<Integer> calculateXDistances(Enemy enemy, List<Hero> heroes) {
        List<Integer> xDistances = new ArrayList<>();
        for (Hero h : heroes) {
            int xDistance = Math.abs(enemy.getPositionEnemies().getX() - h.getPositionHero().getX());
            xDistances.add(xDistance);
        }
        return xDistances; // Returns the list of all calculated x distances
    }

    /**
     * Calculates the absolute distances in the Y-axis between the enemy's tile and each hero's tile.
     *
     * @param enemy the enemy whose position is used
     * @param heroes list of heroes
     * @return a list of y-axis distances
     */
    public List<Integer> calculateYDistances(Enemy enemy, List<Hero> heroes) {
        List<Integer> yDistances = new ArrayList<>();
        for (Hero h : heroes) {
            int yDistance = Math.abs(enemy.getPositionEnemies().getY() - h.getPositionHero().getY());
            yDistances.add(yDistance);
        }
        return yDistances; // Returns the list of all calculated y distances
    }

    /**
     * Calculates the Manhattan distances (sum of absolute x and y differences) between the enemy's tile and each hero's tile.
     *
     * @param enemy the enemy whose position is used
     * @param heroes list of heroes
     * @return a list of Manhattan distances
     */
    public List<Integer> calculateManhattanDistances(Enemy enemy, List<Hero> heroes) {
        List<Integer> totalDistances = new ArrayList<>();
        for (Hero h : heroes) {
            int xDistance = Math.abs(enemy.getPositionEnemies().getX() - h.getPositionHero().getX());
            int yDistance = Math.abs(enemy.getPositionEnemies().getY() - h.getPositionHero().getY());
            int totalDistance = xDistance + yDistance;
            totalDistances.add(totalDistance);
        }
        return totalDistances; // Returns the list with the sums of absolute distances in x and y
    }

    /**
     * Calculates the Euclidean distance between this tile and another tile.
     *
     * @param other the other tile
     * @return the Euclidean distance
     */
    public double distanceTo(Tile other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }

    /**
     * Calculates the direction from this tile toward the destination tile.
     *
     * @param destination the target tile
     * @return the direction to move
     */
    public Direction directionTo(Tile destination) {
        int dx = destination.x - this.x;
        int dy = destination.y - this.y;

        // Prioritize movement in the axis with the greater difference.
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else if (dy != 0) {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
        return Direction.NONE; // Same tile; no movement needed.
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Tile tile = (Tile) obj;
        return x == tile.x && y == tile.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }
}
