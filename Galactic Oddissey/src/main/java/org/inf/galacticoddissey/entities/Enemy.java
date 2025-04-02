package org.inf.galacticoddissey.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Enemy {
    private int x;
    private int y;
    private int tileX;
    private int tileY;
    private int movementPoints;
    private int maxMovementPoints = 3;
    private Image image;
    private int width = 32;
    private int height = 32;

    public Enemy(int startX, int startY, String imagePath) {
        this.tileX = startX;
        this.tileY = startY;
        this.x = tileX * width;
        this.y = tileY * height;
        this.movementPoints = maxMovementPoints;
        this.image = new Image(getClass().getResourceAsStream(imagePath));
    }

    public void resetMovement() {
        movementPoints = maxMovementPoints;
    }

    public void takeTurn(Player player, int[][] tileMap) {
        if (movementPoints <= 0) return;

        // IA movimiento
        int playerTileX = (int) (player.getX() / width);
        int playerTileY = (int) (player.getY() / height);

        // Calcula dirección hacia el jugador
        int dx = Integer.compare(playerTileX, tileX);
        int dy = Integer.compare(playerTileY, tileY);

        // Intenta movimiento diagonal primero
        if (dx != 0 && dy != 0 && canMoveTo(tileX + dx, tileY + dy, tileMap)) {
            move(dx, dy);
        }
        // Despues movimientos rectos
        else if (dx != 0 && canMoveTo(tileX + dx, tileY, tileMap)) {
            move(dx, 0);
        }
        else if (dy != 0 && canMoveTo(tileX, tileY + dy, tileMap)) {
            move(0, dy);
        }
    }

    public boolean canMoveTo(int newTileX, int newTileY, int[][] tileMap) {
        // Verifica los límites del mapa
        if (newTileX < 0 || newTileY < 0 ||
                newTileX >= tileMap[0].length || newTileY >= tileMap.length) {
            return false;
        }
        // Verificar si el tile es transitable (0 = empty en tu mapa)
        return tileMap[newTileY][newTileX] == 0;
    }

    private void move(int dx, int dy) {
        tileX += dx;
        tileY += dy;
        x = tileX * width;
        y = tileY * height;
        movementPoints--;
    }

    public void pintar(GraphicsContext gc) {
        gc.drawImage(image, x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getTileX() { return tileX; }
    public int getTileY() { return tileY; }
    public int getMovementPoints() { return movementPoints; }
}