package Domain.MagicObjet;

import Domain.Entity.Characters.Hero;

public class EnergyShield extends MagicObject {

    // Counter for the Energy Shield (assume an initial value of 0)
    private int energyShieldCounter = 0;

    public EnergyShield(String name, int usageRange) {
        super();
        // You may want to initialize fields from the parameters,
        // but the original code did not use them.
    }

    /**
     * Grants an Energy Shield to the hero.
     * For characters that are within a range of 1 tile.
     *
     * @param hero the hero receiving the shield
     */
    public void gainEnergyShield(Hero hero) {
        // If the MagicObject does not already contain "EnergyShield",
        // add it (assumes MagicObject provides static collection methods)
        if (!MagicObject.contains("EnergyShield")) {
            MagicObject.add("EnergyShield");
        }
        energyShieldCounter = +1; // Sets shield counter to 1 (consider using increment if needed)
        System.out.println("Obtained an Energy Shield");
    }

    /**
     * Activates the Energy Shield special ability.
     * To activate this ability, the hero must have at least one shield.
     *
     * @param hero the hero activating the shield
     */
    public void activateEnergyShield(Hero hero) {
        if (hero.getTakeDamage() <= 4) {
            hero.takeDamage(0);
        } else {
            hero.takeDamage(hero.getTakeDamage() - 4);
        }
        MagicObject.remove("EnergyShield");
        energyShieldCounter = -1;
    }

    /**
     * Returns a string indicating the current Energy Shield count.
     *
     * @return a string with the energy shield count
     */
    public String getEnergyShield() {
        return "Energy Shield Count: " + energyShieldCounter;
    }

    /**
     * Returns the energy shield count as an integer.
     *
     * @return the energy shield count
     */
    public int getEnergyShieldInt() {
        return energyShieldCounter;
    }
}
