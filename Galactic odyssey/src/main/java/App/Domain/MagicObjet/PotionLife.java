package Domain.MagicObjet;

import Domain.Entity.Characters.Hero;
import java.util.Random;

public class PotionLife extends MagicObject {

    // Counter for the number of Life Potions acquired.
    private int lifePotionCounter = 0;

    // The healing value of the Life Potion.
    private int lifePotionValue = 0;

    // Random number generator instance.
    private Random random = new Random();

    public PotionLife(String name, int usageRange) {
        super();
        // Optionally initialize fields using name and usageRange if needed.
    }

    /**
     * Allows the hero to acquire a Life Potion.
     * For characters that are within a range of 1 tile.
     *
     * @param hero the hero acquiring the Life Potion.
     */
    public void acquireLifePotion(Hero hero) {
        // Check if MagicObject (the collection) already contains "PotionLife".
        if (!MagicObject.contains("PotionLife")) {
            MagicObject.add("PotionLife");
        }
        lifePotionCounter++; // Increase the counter by 1.
        System.out.println("Obtained a Life Potion");
    }

    /**
     * Determines the healing effect of the Life Potion based on a random value.
     *
     * @return the healing value of the Life Potion.
     */
    public int getLifePotionEffect() {
        // Generate a random value between 0 and 9.
        int randomValue = random.nextInt(10);
        if (randomValue < 2) {
            lifePotionValue = 3;
        } else if (randomValue < 6) {
            lifePotionValue = 4;
        } else if (randomValue < 9) {
            lifePotionValue = 5;
        }
        return lifePotionValue;
    }

    /**
     * Activates the Life Potion, healing the hero.
     * To activate this special ability, the hero must have at least one potion.
     * Upon activation, the potion is removed and its healing value is added to the hero's current health.
     *
     * @param hero the hero using the Life Potion.
     * @return the new health value of the hero after applying the potion.
     */
    public int activateLifePotion(Hero hero) {
        MagicObject.remove("PotionLife");
        return hero.getHealth() + lifePotionValue;
    }

    /**
     * Returns a string indicating the current number of Life Potions.
     *
     * @return a string with the Life Potion count.
     */
    public String getLifePotionStatus() {
        return "Number of Life Potions: " + lifePotionCounter;
    }

    /**
     * Returns the healing value of the Life Potion.
     * This is useful, for instance, when an enemy pirate tries to steal the potion.
     *
     * @return the healing value of the Life Potion.
     */
    public int getLifePotionValue() {
        return lifePotionValue;
    }
}

