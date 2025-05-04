package levels;

public abstract class BaseLevel {
    // This method will initialize all the elements of the level:
    public abstract void initLevel();

    // You can add other common methods, such as cleaning up or finishing the level:
    public void finishLevel() {
        // Common logic to finish the level (e.g., clear entities, play animation, etc.)
    }
}
