package com.example.ani;

import Class.Player;
import Class.Enemy;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    private GraphicsContext graphics;
    private Group root;
    private Scene scene;
    private Canvas canvas;
    private Player player;
    private static int[][] tileMap;
    private static int tileWidth = 32;
    private static int tileHeight = 32;
    private static int mapWidth = 10;
    private static int mapHeight = 10;
    private Image tilesetImage;
    private int tilesetColumns = 3;
    private static final int TILE_SIZE = 32;
    private List<Enemy> enemies = new ArrayList<>();
    private boolean playerTurn = true;
    private int currentEnemyIndex = 0;
    public static boolean up, down, right, left;

  
    public static boolean isTileWalkable(int tileX, int tileY) {
        // Verificar q las coordenadas estén dentro del mapa
        if (tileX < 0 || tileY < 0 || tileX >= mapWidth || tileY >= mapHeight) {
            return false;
        }
        // Verificar el tipo de tile (0 = empty/vacío es transitable)
        return tileMap[tileY][tileX] == 0;
    }

    public void start(Stage stage) throws Exception {
        loadTiledMap();
        initializeComponents();
        setupEventHandlers();
        createEnemies();

        stage.setScene(scene);
        stage.setTitle("Galactic Odyssey: The Cosmic Enigma");
        stage.show();

        startGameLoop();
    }

    private void createEnemies() {
            //Ruta del enemigo
            String enemyImagePath = "/org/inf/galacticoddissey/enemigo.png"; // Ajusta esta ruta

            //Crear los enemigos
            enemies.add(new Enemy(5, 5, enemyImagePath));
            enemies.add(new Enemy(8, 2, enemyImagePath));

    }


    private void updateGame() {
        if (playerTurn) {
            player.move();
        } else {
            if (currentEnemyIndex < enemies.size()) {
                Enemy currentEnemy = enemies.get(currentEnemyIndex);
                currentEnemy.takeTurn(player, tileMap);
                currentEnemyIndex++;

                if (currentEnemyIndex >= enemies.size()) {
                    endEnemyTurn();
                }
            }
        }
    }

    private void endPlayerTurn() {
        playerTurn = false;
        currentEnemyIndex = 0;
        player.resetMovement();
    }

    private void endEnemyTurn() {
        playerTurn = true;
        enemies.forEach(Enemy::resetMovement);
        player.startTurn();
    }

    private void initializeComponents() {
        player = new Player(1, 1, "/org/inf/galacticoddissey/personaje.png");
        canvas = new Canvas(mapWidth * tileWidth, mapHeight * tileHeight);
        graphics = canvas.getGraphicsContext2D();
        root = new Group(canvas);
        scene = new Scene(root);
    }

    private ImageView createTileView(String type, Image tilesetImage) {
        int tileIndex = 0;
        switch(type) {
            case "star": tileIndex = 1; break;
            case "planet": tileIndex = 2; break;
            case "asteroid": tileIndex = 3; break;
            case "nebula": tileIndex = 4; break;
            case "spaceStation": tileIndex = 5; break;
        }
        ImageView tileView = new ImageView(tilesetImage);
        tileView.setViewport(new Rectangle2D(tileIndex * TILE_SIZE, 0, TILE_SIZE, TILE_SIZE));
        return tileView;
    }

    private void createFallbackMap() {
        mapWidth = 10;
        mapHeight = 10;
        tileWidth = 32;
        tileHeight = 32;
        tileMap = new int[mapHeight][mapWidth];

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                tileMap[y][x] = (x == 0 || y == 0 || x == mapWidth-1 || y == mapHeight-1) ? 1 : 0;
            }
        }
    }

    private void loadTiledMap() {
        try {
            InputStream is = getClass().getResourceAsStream("/org/inf/galacticoddissey/map.json");
            JSONObject mapData = new JSONObject(new JSONTokener(is));

            String tilesetPath = "/org/inf/galacticoddissey/tileset.png";
            tilesetImage = new Image(getClass().getResourceAsStream(tilesetPath));

            mapWidth = mapData.getJSONObject("grid").getInt("columns");
            mapHeight = mapData.getJSONObject("grid").getInt("rows");
            tileWidth = mapData.getInt("tileSize");
            tileHeight = mapData.getInt("tileSize");
            tileMap = new int[mapHeight][mapWidth];

            JSONArray rows = mapData.getJSONArray("tiles");
            for (int y = 0; y < rows.length(); y++) {
                JSONArray cols = rows.getJSONArray(y);
                for (int x = 0; x < cols.length(); x++) {
                    JSONObject tileData = cols.getJSONObject(x);
                    String tileType = tileData.getString("type");

                    switch(tileType) {
                        case "star": tileMap[y][x] = 1; break;
                        case "planet": tileMap[y][x] = 2; break;
                        case "asteroid": tileMap[y][x] = 3; break;
                        case "nebula": tileMap[y][x] = 4; break;
                        case "spaceStation": tileMap[y][x] = 5; break;
                        default: tileMap[y][x] = 0;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading map: " + e.getMessage());
            e.printStackTrace();
            createFallbackMap();
            if (tilesetImage == null) {
                tilesetImage = new Image(getClass().getResourceAsStream("/path/to/default/tileset.png"));
            }
        }
    }

    private void setupEventHandlers() {
        scene.setOnKeyPressed(e -> {
            if (playerTurn) {
                switch (e.getCode()) {
                    case RIGHT: right = true; break;
                    case LEFT: left = true; break;
                    case UP: up = true; break;
                    case DOWN: down = true; break;
                    case ENTER: endPlayerTurn(); break;
                }
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case RIGHT: right = false; break;
                case LEFT: left = false; break;
                case UP: up = false; break;
                case DOWN: down = false; break;
            }
        });
    }

    private void startGameLoop() {
        new AnimationTimer() {
            public void handle(long now) {
                updateGame();
                render();
            }
        }.start();
    }

    private void render() {
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                int tileId = tileMap[y][x];
                if (tileId >= 0) {
                    int srcX = (tileId % tilesetColumns) * tileWidth;
                    int srcY = (tileId / tilesetColumns) * tileHeight;
                    graphics.drawImage(tilesetImage, srcX, srcY, tileWidth, tileHeight,
                            x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                }
            }
        }
        enemies.forEach(enemy -> enemy.pintar(graphics));
        player.pintar(graphics);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
