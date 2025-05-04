package Domain.MagicObjet;

import Domain.Entity.Characters.Hero;

public class GasMask extends MagicObject {

    // Counter for the Gas Mask
    private int gasMaskCounter = 0;

    public GasMask(String name, int usageRange) {
        super();
        // Optionally initialize fields from the parameters as needed.
    }

    /**
     * Grants a Gas Mask to the hero.
     * Negates the effect of the toxic nebula.
     *
     * @param hero the hero acquiring the Gas Mask.
     */
    public void gainGasMask(Hero hero) {
        // If the MagicObject does not already contain "GasMask", add it.
        if (!MagicObject.contains("GasMask")) {
            MagicObject.add("GasMask");
        }
        gasMaskCounter = +1; // Sets counter to 1. (Consider using gasMaskCounter++ if you intend to accumulate multiple shields.)
        System.out.println("Obtained a Gas Mask");
    }

    /**
     * Activates the Gas Mask.
     * When activated, this special ability negates the toxic nebula damage.
     *
     * @param hero the hero activating the Gas Mask.
     */
    public void activateGasMask(Hero hero) {
        // To activate this ability, the hero must have at least one Gas Mask.
        // When activated, the toxic cloud's (nebula) damage is nullified.
        boolean damageNebula = false;  // This can be used to indicate the nebula's effect is disabled.
        MagicObject.remove("GasMask");
        gasMaskCounter = -1; // Resets the counter (or sets it to a value that indicates the mask has been used)
    }

    /**
     * Returns a string with the current count of Gas Masks.
     *
     * @return Number of Gas Masks.
     */
    public String getGasMask() {
        return "Number of Gas Masks: " + gasMaskCounter;
    }
}
