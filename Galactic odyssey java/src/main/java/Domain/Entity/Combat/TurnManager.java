package Domain.Entity.Combat;

import Domain.Entity.Characters.Enemies.EnemyEntity;
import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.Characters.Players.PlayerEntity;
import Domain.Entity.Elements.ObjetosMagicos.Item;
import Domain.Entity.Elements.ObjetosMagicos.ItemEntity;
import Domain.Entity.Elements.Trampas.NebulosaToxica;
import Domain.Entity.Elements.Trampas.TrampaBase;

import Domain.Entity.EntityType;
import Domain.Levels.Level1;
import View.UI.PlayerHUD;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

import java.util.*;



public class TurnManager {
    private static final double TILE_SIZE = 56;
    private List<PlayerEntity> players = new ArrayList<>();
    private List<EnemyEntity> enemies = new ArrayList<>();
    private List<PlayerHUD> huds = new ArrayList<>();
    private List<NebulosaToxica> nebulosasToxicas = new ArrayList<>();

    private boolean playerTurn = true;
    private int currentPlayerIndex = 0;
    private int currentEnemyIndex = 0;
    private boolean processingTurn = false;

    public static Map<Entity, EnemyEntity> ENEMY_LOGIC_MAP = new HashMap<>();

    private final int mapWidth = 60 * 56;
    private final int mapHeight = 30 * 56;

    public void addPlayer(PlayerEntity player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public void addHUD(PlayerHUD hud) {
        huds.add(hud);
    }

    public PlayerEntity getCurrentPlayer() {
        if (players.isEmpty() || currentPlayerIndex >= players.size()) {
            return null;
        }
        return players.get(currentPlayerIndex);
    }

    public void addEnemy(EnemyEntity enemy) {
        if (!enemies.contains(enemy)) {
            enemies.add(enemy);
            ENEMY_LOGIC_MAP.put(enemy.getEntity(), enemy);
        }
    }

    public List<EnemyEntity> getEnemies() {
        return new ArrayList<>(enemies);
    }

    public EnemyEntity getCurrentEnemy() {
        if (enemies.isEmpty() || currentEnemyIndex >= enemies.size()) {
            return null;
        }
        return enemies.get(currentEnemyIndex);
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void endTurn() {
        if (processingTurn) return;
        processingTurn = true;

        try {
            if (playerTurn) {
                handlePlayerTurnEnd();
            } else {
                handleEnemyTurnEnd();
            }
        } finally {
            processingTurn = false;
        }
    }

    private void handlePlayerTurnEnd() {
        PlayerEntity current = getCurrentPlayer();
        if (current != null) {
            checkPlayerItemPickup(current);
            checkTrapEffects(current);
            current.endTurn();
        }

        int nextIndex = findNextAlivePlayer(currentPlayerIndex + 1);

        if (nextIndex == -1) {
            gameOver();
            return;
        }

        if (nextIndex <= currentPlayerIndex) {
            startEnemyPhase();
        } else {
            currentPlayerIndex = nextIndex;
            startPlayerTurn();
        }

    }

    private void handleEnemyTurnEnd() {
        currentEnemyIndex++;

        if (currentEnemyIndex >= enemies.size()) {
            startPlayerPhase();
        } else {
            startNextEnemyTurn();
        }
    }

    private int findNextAlivePlayer(int startFrom) {
        int size = players.size();
        for (int i = 0; i < size; i++) {
            int index = (startFrom + i) % size;
            if (players.get(index).getHero().isAlive()) {
                return index;
            }
        }
        return -1;
    }


    private void startPlayerPhase() {
        System.out.println("[TURN MANAGER] INICIANDO FASE DE JUGADORES");
        playerTurn = true;
        currentEnemyIndex = 0;

        currentPlayerIndex = findNextAlivePlayer(0);
        if (currentPlayerIndex != -1) {
            startPlayerTurn();
        } else {
            gameOver();
        }
    }

    private void startPlayerTurn() {
        System.out.println("[DEBUG] Iniciando el turno del jugador.");
        Level1 level1 = new Level1();
        level1.updateTraps();

        PlayerEntity player = getCurrentPlayer();
        if (player != null) {
            System.out.println("[TURN MANAGER] Turno del jugador: " + player.getHero().name);

            player.startTurn();
            showOnlyHUDOf(player);
            updateAllHUDs();

            // Debug posición antes de centrar cámara
            double x = player.getEntity().getX();
            double y = player.getEntity().getY();
            System.out.println("Posición jugador antes de cámara: x=" + x + ", y=" + y);

            FXGL.getGameTimer().runOnceAfter(() -> {
                System.out.println("Centrando cámara en x=" + player.getEntity().getX() + ", y=" + player.getEntity().getY());
                focusCameraOn(player.getEntity());
            }, Duration.seconds(0.2));
        }
    }

    private void startEnemyPhase() {
        System.out.println("[TURN MANAGER] INICIANDO FASE DE ENEMIGOS (" + enemies.size() + " enemigos)");
        playerTurn = false;
        currentPlayerIndex = 0;
        currentEnemyIndex = 0;

        hideAllHUDs();

        if (!enemies.isEmpty()) {
            startNextEnemyTurn();
        } else {
            System.out.println("[TURN MANAGER] No hay enemigos, pasando a jugadores");
            startPlayerPhase();
        }
    }

    private void startNextEnemyTurn() {
        EnemyEntity enemy = getCurrentEnemy();
        if (enemy == null) {
            System.out.println("[TURN MANAGER] Enemigo actual es null, terminando fase");
            startPlayerPhase();
            return;
        }

        if (!enemy.getLogic().isAlive()) {
            System.out.println("[TURN MANAGER] Enemigo " + enemy.getLogic().name + " está muerto, saltando");
            endTurn();
            return;
        }

        System.out.println("[TURN MANAGER] Iniciando turno para enemigo " + enemy.getLogic().name +
                " (" + (currentEnemyIndex + 1) + "/" + enemies.size() + ")");

        focusCameraOn(enemy.getEntity());

        enemy.takeTurn(() -> {
            endTurn();
        });
    }

    private void gameOver() {
        FXGL.getDialogService().showMessageBox("Game Over - Todos los jugadores han muerto",
                () -> FXGL.getGameController().gotoMainMenu());
    }

    private void showOnlyHUDOf(PlayerEntity player) {
        for (PlayerHUD hud : huds) {
            hud.setVisible(hud.playerEntity == player);
        }
    }

    private void hideAllHUDs() {
        for (PlayerHUD hud : huds) {
            hud.setVisible(false);
        }
    }

    public void updateAllHUDs() {
        for (PlayerHUD hud : huds) {
            hud.updateHUD();
        }
    }

    public void removeEnemy(EnemyEntity enemy) {
        enemies.remove(enemy);
        ENEMY_LOGIC_MAP.remove(enemy.getEntity());
    }


    public boolean isTileOccupied(int x, int y) {
        // Checar jugadores
        for (PlayerEntity player : players) {
            if (player.getHero().getX() == x && player.getHero().getY() == y && player.getHero().isAlive()) {
                return true;
            }
        }
        // Checar enemigos
        for (EnemyEntity enemy : enemies) {
            if (enemy.getLogic().getX() == x && enemy.getLogic().getY() == y && enemy.getLogic().isAlive()) {
                return true;
            }
        }
        // Checar obstáculos
        for (Entity obs : FXGL.getGameWorld().getEntitiesByType(EntityType.OBSTACULOS)) {
            int ox = (int) (obs.getX() / TILE_SIZE);
            int oy = (int) (obs.getY() / TILE_SIZE);
            if (ox == x && oy == y) {
                return true;
            }
        }
        return false;
    }

    public void focusCameraOn(Entity entity) {
        FXGL.getGameScene().getViewport().unbind();
        FXGL.getGameScene().getViewport().bindToEntity(entity, FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);
        FXGL.getGameScene().getViewport().setLazy(true); // movimiento suave
        FXGL.getGameScene().getViewport().setBounds(0, 0, mapWidth, mapHeight); // evita que se salga
    }

    private void checkPlayerItemPickup(PlayerEntity player) {
        FXGL.getGameWorld().getEntitiesByType(EntityType.ITEM).forEach(itemEntity -> {
            if (player.getEntity().isColliding(itemEntity)) {
                ItemEntity itemComp = itemEntity.getComponent(ItemEntity.class);
                if (itemComp != null) {
                    Item item = itemComp.getItem();

                    // Agregar el item al inventario del héroe
                    player.getHero().getInventory().add(item);

                    // Actualizar HUD para mostrar el inventario actualizado
                    PlayerHUD hud = player.getHUD();
                    if (hud != null) {
                        hud.updateHUD();
                    }

                    // Remover el item del mundo para que desaparezca
                    FXGL.getGameWorld().removeEntity(itemEntity);

                    System.out.println("Item recogido: " + item.getName());
                }
            }
        });
    }

    private void checkTrapEffects(PlayerEntity player) {
        FXGL.getGameWorld().getEntitiesByType(EntityType.TRAP).forEach(trapEntity -> {
            Component comp = trapEntity.getComponent(Component.class);  // Obtener el componente genérico

            if (comp instanceof TrampaBase trap) {  // Verificar que implemente TrampaBase
                trap.applyEffect(players.stream().map(PlayerEntity::getHero).toList());
            }
        });
    }

    public void verificarTrampas(PlayerEntity player) {
        Hero hero = player.getHero(); // ahora sí tienes un Hero
        for (NebulosaToxica nebulosa : nebulosasToxicas) {
            nebulosa.applyEffect(List.of(hero));
        }
    }

}
