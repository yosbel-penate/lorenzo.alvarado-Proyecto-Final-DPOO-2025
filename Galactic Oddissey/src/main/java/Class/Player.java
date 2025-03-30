package Class;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.inf.galacticoddissey.HelloApplication;

public class Player {
    private double x; // Coordenada X en píxeles
    private double y; // Coordenada Y en píxeles
    private int tileX; // Posición X en tiles
    private int tileY; // Posición Y en tiles
    private Image image;
    private double width;
    private double height;
    private int movementPoints;
    private int maxMovementPoints = 3;
    private boolean isMyTurn = true;

    public Player(int startTileX, int startTileY, String imagePath) {
        this.tileX = startTileX;
        this.tileY = startTileY;
        this.width = 32;
        this.height = 32;
        updatePixelPosition(); // Inicializa x e y basado en tileX/tileY


            this.image = new Image(getClass().getResourceAsStream(imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();


        this.movementPoints = maxMovementPoints;
    }

    private void updatePixelPosition() {
        this.x = tileX * width;
        this.y = tileY * height;
    }

    public void resetMovement() {
        movementPoints = maxMovementPoints;
    }

    public void endTurn() {
        isMyTurn = false;
        movementPoints = 0;
    }

    public void startTurn() {
        isMyTurn = true;
        movementPoints = maxMovementPoints;
    }

    public boolean canMove() {
        return isMyTurn && movementPoints > 0;
    }

    public void move() {
        if (!canMove()) return;

        int newTileX = tileX;
        int newTileY = tileY;

        if (HelloApplication.right) newTileX++;
        if (HelloApplication.left) newTileX--;
        if (HelloApplication.up) newTileY--;
        if (HelloApplication.down) newTileY++;

        if ((newTileX != tileX || newTileY != tileY) &&
                HelloApplication.isTileWalkable(newTileX, newTileY)) {

            tileX = newTileX;
            tileY = newTileY;
            updatePixelPosition();
            movementPoints--;

            // Resetea las teclas después de q se mueve
            HelloApplication.right = false;
            HelloApplication.left = false;
            HelloApplication.up = false;
            HelloApplication.down = false;
        }
    }

    public void pintar(GraphicsContext gc) {
        gc.drawImage(image, x, y, width, height);
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // Setters para actualizar posición
    public void setTilePosition(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
        updatePixelPosition();
    }
}