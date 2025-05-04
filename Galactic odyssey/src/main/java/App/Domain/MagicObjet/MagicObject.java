package Domain.MagicObjet;

import java.util.ArrayList;
import java.util.List;

public class MagicObject {

    // Array of elements (if needed)
    private int[] elements = new int[3];

    // Basic properties
    protected String name;
    protected int usageRange; // Originally "RangoUso"

    // A random value between 0 and 9
    int randomValue = (int)(Math.random() * 10);

    // Life potion value: gives a random value for the increase in life
    int potionLife;

    // Collection of magic object names (you can use this to keep track of which magic objects the character has)
    private static List<String> magicObjects = new ArrayList<>();
    public static boolean contains(String objectName) {
        return magicObjects.contains(objectName);
    }

    public static void add(String objectName) {
        magicObjects.add(objectName);
    }

    public static void remove(String objectName) {
        magicObjects.remove(objectName);
    }
    public List<String> getMagicObjects() {
        return magicObjects;
    }

    // General counters for magical objects
    // (e.g., used to add magic objects to the characters)
    int lifePotionCounter = 0;   // Counter for life potions
    int energyShieldCounter = 0; // Counter for energy shields
    int plasmaPumpCounter = 0;   // Counter for plasma bombs
    int gasMaskCounter = 0;      // Counter for gas masks

    /**
     * Default constructor.
     * Note: The assignments below do not actually initialize the fields
     * since no parameters are provided. Consider updating this constructor
     * if you need to pass initial values.
     */
    public MagicObject() {
        this.name = name;
        this.usageRange = usageRange;
    }
}
