package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.EnemyEntity;
import Domain.Entity.Combat.TurnManager;
import Domain.Entity.Elements.ObjetosMagicos.GlobalInventory;
import Domain.Entity.Elements.ObjetosMagicos.Item;
import Domain.Entity.Elements.ObjetosMagicos.ItemEntity;
import Domain.Entity.EntityType;
import View.UI.PlayerHUD;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;

public class PlayerEntity {

    private Entity player;
    private final int TILE_SIZE = 56;
    private Hero hero;
    private int movimientosDisponibles;
    private TurnManager turnManager;
    private int mapWidth = 60 * 56;
    private int mapHeight = 30 * 56;
    private int gridX;
    private int gridY;
    private static boolean inputsRegistered = false;
    private Entity entity;
    private PlayerHUD hud;
    private List<Item> inventory = new ArrayList<>();
    private static Hero currentHero;
    public PlayerEntity(String heroType, int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;

        switch (heroType) {
            case "Andrómeda":
                hero = new Andromeda();
                break;
            case "Aurora":
                hero = new Aurora();
                break;
            case "Blaze":
                hero = new Blaze();
                break;
            case "Capitán Orion":
                hero = new CapOrion();
                break;
            case "Comet":
                hero = new Comet();
                break;
            case "Cygnus":
                hero = new Cygnus();
                break;
            case "Deimos":
                hero = new Deimos();
                break;
            case "Eclipse":
                hero = new Eclipse();
                break;
            case "Lyra":
                hero = new Lyra();
                break;
            case "Luna":
                hero = new Luna();
                break;
            case "Nebula":
                hero = new Nebula();
                break;
            case "Nova":
                hero = new Nova();
                break;
            case "Orion Jr.":
                hero = new OrionJr();
                break;
            case "Phobos":
                hero = new Phobos();
                break;
            case "Quasar":
                hero = new Quasar();
                break;
            case "Solara":
                hero = new Solara();
                break;
            case "Stella":
                hero = new Stella();
                break;
            case "Titan":
                hero = new Titan();
                break;
            case "Vortex":
                hero = new Vortex();
                break;
            case "Zorak":
                hero = new Zorak();
                break;
            default:
                hero = new CapOrion();
                break;
        }
    }

    public void setTurnManager(TurnManager manager) {
        this.turnManager = manager;
    }

    public void startTurn() {
        movimientosDisponibles = hero.moveRange;
        player.getViewComponent().setOpacity(1.0);

        Text text = new Text("Turno de " + hero.name);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", 24));
        text.setEffect(new DropShadow(4, Color.BLACK));

        Rectangle background = new Rectangle(300, 50);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(Color.web("#0a0e3f", 0.8));

        StackPane notification = new StackPane(background, text);
        notification.setTranslateX(250);
        notification.setTranslateY(40);
        notification.setOpacity(0);

        FXGL.getGameScene().addUINode(notification);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), notification);
        fadeIn.setToValue(1);
        fadeIn.play();

        PauseTransition wait = new PauseTransition(Duration.seconds(1));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), notification);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> FXGL.getGameScene().removeUINode(notification));

        wait.setOnFinished(e -> fadeOut.play());
        wait.play();
    }

    public void endTurn() {
        movimientosDisponibles = 0;
        player.getViewComponent().setOpacity(0.6);
    }


    public void spawnPlayer() {
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(gridX * TILE_SIZE, gridY * TILE_SIZE)
                .view(FXGL.texture("personaje.png", 56, 56))
                .bbox(new HitBox("FOOTBOX", BoundingShape.box(56, 56)))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        hero.setPosition(gridX, gridY);


        if (!inputsRegistered) {
            initInput();
            inputsRegistered = true;
        }
    }


    public void centerCameraOnPlayer() {
        double playerCenterX = player.getX() + TILE_SIZE / 2.0;
        double playerCenterY = player.getY() + TILE_SIZE / 2.0;

        double viewportWidth = FXGL.getAppWidth();
        double viewportHeight = FXGL.getAppHeight();

        double cameraX = playerCenterX - viewportWidth / 2.0;
        double cameraY = playerCenterY - viewportHeight / 2.0;

        FXGL.getGameScene().getViewport().setX(cameraX);
        FXGL.getGameScene().getViewport().setY(cameraY);
    }
    // Verifica si las nuevas coordenadas están dentro de los límites del mapa
    private boolean isWithinMapBounds(int tileX, int tileY) {
        // Limitar a las dimensiones del mapa
        return tileX >= 0 && tileX < mapWidth / TILE_SIZE && tileY >= 0 && tileY < mapHeight / TILE_SIZE;
    }
    private void moveByGrid(int dx, int dy) {
        if (!turnManager.isPlayerTurn() || movimientosDisponibles <= 0 || turnManager.getCurrentPlayer() != this) {
            return;
        }

        int nextTileX = hero.getX() + dx;
        int nextTileY = hero.getY() + dy;

        double newX = player.getX() + dx * TILE_SIZE;
        double newY = player.getY() + dy * TILE_SIZE;

        // Verificación de colisión con obstáculos antes de moverse
        List<Entity> obstacles = FXGL.getGameWorld().getEntitiesByType(EntityType.OBSTACULOS);
        Rectangle2D futureBB = new Rectangle2D(newX, newY, player.getWidth(), player.getHeight());

        boolean blocked = obstacles.stream().anyMatch(o -> {
            Rectangle2D obBB = new Rectangle2D(o.getX(), o.getY(), o.getWidth(), o.getHeight());
            return futureBB.intersects(obBB);
        });

        if (blocked) {
            System.out.println("¡Colisión con obstáculo! Movimiento bloqueado.");
            return; // Detener movimiento si hay un obstáculo
        }
        List<Entity> players = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);

        boolean blockedByPlayer = players.stream().anyMatch(p -> {
            Rectangle2D playerBB = new Rectangle2D(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            return futureBB.intersects(playerBB);
        });

        if (blockedByPlayer) {
            System.out.println("¡Colisión con otro jugador! Movimiento bloqueado.");
            return; // Detener movimiento si hay otro jugador en la misma celda
        }
// Verificar si hay un enemigo en la posición futura
        List<Entity> enemies = FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY);

        boolean blockedByEnemy = enemies.stream().anyMatch(e -> {
            Rectangle2D enemyBB = new Rectangle2D(e.getX(), e.getY(), e.getWidth(), e.getHeight());
            return futureBB.intersects(enemyBB);
        });

        if (blockedByEnemy) {
            System.out.println("¡Un enemigo está ocupando esta casilla! Movimiento bloqueado.");
            return; // Detener movimiento si hay un enemigo en la misma celda
        }
        // Solo se mueve si no hay colisión
        player.setPosition(newX, newY);
        hero.setPosition(nextTileX, nextTileY);
        movimientosDisponibles--;

        checkItemPickup(); // Revisar si hay objetos para recoger
    }


    private void initInput() {
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn()) {
                    currentPlayer.moveByGrid(1, 0);
                    turnManager.verificarTrampas(currentPlayer);  }
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn()) {
                    currentPlayer.moveByGrid(-1, 0);
                    turnManager.verificarTrampas(currentPlayer);  }
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn()) {
                    currentPlayer.moveByGrid(0, -1);
                    turnManager.verificarTrampas(currentPlayer);  }
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn()) {
                    currentPlayer.moveByGrid(0, 1);
                    turnManager.verificarTrampas(currentPlayer); }
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("End Turn") {
            @Override
            protected void onActionBegin() {
                if (turnManager.isPlayerTurn() && turnManager.getCurrentPlayer() != null) {
                    turnManager.getCurrentPlayer().endTurn();
                    turnManager.endTurn();
                }
            }
        }, KeyCode.ENTER);

        FXGL.getInput().addAction(new UserAction("Attack") {
            @Override
            protected void onActionBegin() {
                if (!turnManager.isPlayerTurn()) {
                    return;
                }

                int range = hero.attackRange;

                double mouseX = FXGL.getInput().getMouseXWorld();
                double mouseY = FXGL.getInput().getMouseYWorld();

                int tileX = (int) (mouseX / TILE_SIZE);
                int tileY = (int) (mouseY / TILE_SIZE);

                for (EnemyEntity enemy : turnManager.getEnemies()) {
                    if (!enemy.getLogic().isAlive()) continue;

                    int ex = enemy.getLogic().getX();
                    int ey = enemy.getLogic().getY();

                    int distanceToPlayer = hero.getDistanceTo(ex, ey);

                    if (distanceToPlayer <= range) {
                        if (ex == tileX && ey == tileY) {
                            int damage = (int) (Math.random() * (hero.maxDamage - hero.minDamage + 1)) + hero.minDamage;
                            enemy.getLogic().health -= damage;
                            System.out.println(hero.name + " ataca y causa " + damage + " de daño. Vida del enemigo: " + enemy.getLogic().health);

                            enemy.showDamage(damage);

                            if (!enemy.getLogic().isAlive()) {
                                FXGL.getGameWorld().removeEntity(enemy.getEntity());
                                turnManager.removeEnemy(enemy);
                                System.out.println("¡Enemigo eliminado!");
                            }

                            endTurn();
                            turnManager.endTurn();
                            break;
                        }
                    }
                }
            }
        }, MouseButton.PRIMARY);


        FXGL.getInput().addAction(new UserAction("Special Ability") {
            @Override
            protected void onActionBegin() {
                PlayerEntity currentPlayer = turnManager.getCurrentPlayer();
                if (currentPlayer != null && turnManager.isPlayerTurn() &&
                        currentPlayer.getHero().canUseSpecial()) {
                    currentPlayer.getHero().useSpecialAbility();
                    currentPlayer.getHero().resetCooldown();
                    currentPlayer.endTurn();
                    turnManager.endTurn();
                }
            }
        }, KeyCode.E);
        getInput().addAction(new UserAction("Use Potion") {
            @Override
            protected void onActionBegin() {
                useItemByType("pocion");
            }
        }, KeyCode.DIGIT1);

        getInput().addAction(new UserAction("Use Shield") {
            @Override
            protected void onActionBegin() {
                useItemByType("escudoEnergia");
            }
        }, KeyCode.DIGIT2);

        getInput().addAction(new UserAction("Use Bomb") {
            @Override
            protected void onActionBegin() {
                useItemByType("Bomb");
            }
        }, KeyCode.DIGIT3);
    }

    private void useItemByType(String type) {
        System.out.println("Buscando ítem de tipo: " + type);
        System.out.println("Inventario actual:");
        for (Item item : GlobalInventory.getItems()) {
            System.out.println("- " + item.getName() + " (tipo: " + item.getType() + ")");
        }

        Optional<Item> itemToUse = GlobalInventory.getItems().stream()
                .filter(item -> item.getType() != null && item.getType().equalsIgnoreCase(type))
                .findFirst();

        if (itemToUse.isPresent()) {
            Item item = itemToUse.get();
            item.use(this.hero);  // ⚠️ Cambiado de getCurrentHero() a this.hero

            GlobalInventory.removeItem(item);

            if (hud != null) {
                hud.updateInventoryImages();
            }

            System.out.println(hero.getName() + " usó " + item.getName());

        } else {
            System.out.println("No tienes: " + type);
        }
    }

    public Entity getEntity() {
        return player;
    }

    public Hero getHero() {
        return hero;
    }
    public boolean isTileOccupied(int tileX, int tileY) {
        return FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).stream()
                .anyMatch(e -> (int) (e.getX() / TILE_SIZE) == tileX && (int) (e.getY() / TILE_SIZE) == tileY)
                || FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).stream()
                .anyMatch(e -> (int) (e.getX() / TILE_SIZE) == tileX && (int) (e.getY() / TILE_SIZE) == tileY);
    }

    public static boolean isTileBlocked(int tileX, int tileY) {
        int TILE_SIZE = 56;
        return FXGL.getGameWorld()
                .getEntitiesByType(EntityType.OBSTACULOS)
                .stream()
                .anyMatch(e -> (int) (e.getX() / TILE_SIZE) == tileX && (int) (e.getY() / TILE_SIZE) == tileY);
    }


    private void checkItemPickup() {
        int tileX = hero.getX();
        int tileY = hero.getY();

        List<Entity> itemsOnTile = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.ITEM)
                .stream()
                .filter(e -> (int) (e.getX() / TILE_SIZE) == tileX && (int) (e.getY() / TILE_SIZE) == tileY)
                .toList();

        for (Entity itemEntity : itemsOnTile) {
            ItemEntity itemComponent = itemEntity.getComponentOptional(ItemEntity.class).orElse(null);
            if (itemComponent == null) continue;

            Item item = itemComponent.getItem();

            GlobalInventory.addItem(item);             // ✅ Agrega al inventario global
            FXGL.getGameWorld().removeEntity(itemEntity);
            System.out.println("Item recogido: " + item.getName());

            if (hud != null)
                hud.updateInventoryImages();           // ✅ HUD ya no depende del héroe
        }
    }

    public void pickUpItem(Entity itemEntity) {
        Item item = (Item) itemEntity.getObject("item");
        addItemToInventory(item);
        itemEntity.removeFromWorld();

        if (hud != null) {
            hud.updateInventoryImages();
        }
    }

    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public void useItem(Item item) {
        if (inventory.contains(item)) {
            item.use(hero); // Usa el Hero interno
            inventory.remove(item);
            if (hud != null) {
                hud.updateInventoryImages();
            }
        }
        if (hero == null) {
            System.out.println("No hay un héroe seleccionado. No se puede usar el ítem.");
            return;}
    }

    public List<Item> getInventory() {
        return inventory;
    }


    public void setHUD(PlayerHUD hud) {
        this.hud = hud;
    }

    public PlayerHUD getHUD() {
        return hud;
    }

    private void showTurnNotification(String message) {
        Text text = new Text(message);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", 24));
        text.setEffect(new DropShadow(4, Color.BLACK));

        Rectangle bg = new Rectangle(300, 50);
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        bg.setFill(Color.web("#0a0e3f", 0.8));

        StackPane notification = new StackPane(bg, text);
        notification.setTranslateX(250);
        notification.setTranslateY(40);
        notification.setOpacity(0);
        FXGL.getGameScene().addUINode(notification);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), notification);
        fadeIn.setToValue(1);

        PauseTransition wait = new PauseTransition(Duration.seconds(1));
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), notification);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> FXGL.getGameScene().removeUINode(notification));

        fadeIn.play();
        wait.setOnFinished(e -> fadeOut.play());
        wait.play();
    }

    public void onKeyPress(int keyCode) {
        switch (keyCode) {
            case 1:
                useItemByType("Potion");
                break;
            case 2:
                useItemByType("Shield");
                break;
            case 3:
                useItemByType("Bomb");
                break;
        }
    }
    public static Hero getCurrentHero() {
        return currentHero;
    }

    public static void setCurrentHero(Hero hero) {
        currentHero = hero;
    }

}

