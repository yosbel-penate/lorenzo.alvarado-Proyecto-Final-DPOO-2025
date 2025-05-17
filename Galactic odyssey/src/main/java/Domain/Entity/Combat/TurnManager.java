package Domain.Entity.Combat;

import Domain.Entity.Characters.Enemies.EnemyEntity;
import Domain.Entity.Characters.Players.PlayerEntity;
import View.UI.PlayerHUD;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.*;

public class TurnManager {
    private List<PlayerEntity> players = new ArrayList<>();
    private List<EnemyEntity> enemies = new ArrayList<>();
    private List<PlayerHUD> huds = new ArrayList<>();

    private boolean playerTurn = true;
    private int currentPlayerIndex = 0;
    private int currentEnemyIndex = 0;
    private boolean processingTurn = false;

    public static Map<Entity, EnemyEntity> ENEMY_LOGIC_MAP = new HashMap<>();

    // Agrega un jugador y su HUD
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
        for (int i = startFrom; i < players.size(); i++) {
            if (players.get(i).getHero().isAlive()) {
                return i;
            }
        }

        for (int i = 0; i < startFrom && i < players.size(); i++) {
            if (players.get(i).getHero().isAlive()) {
                return i;
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
        PlayerEntity player = getCurrentPlayer();
        if (player != null) {
            System.out.println("[TURN MANAGER] Turno del jugador: " + player.getHero().name);
            player.startTurn();
            showOnlyHUDOf(player);
            updateAllHUDs(); // por si hay cambios de estado
        }
    }

    private void startEnemyPhase() {
        System.out.println("[TURN MANAGER] INICIANDO FASE DE ENEMIGOS (" + enemies.size() + " enemigos)");
        playerTurn = false;
        currentPlayerIndex = 0;
        currentEnemyIndex = 0;

        hideAllHUDs(); // Ocultar HUDs durante turno enemigo

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
        enemy.takeTurn();
    }

    private void gameOver() {
        FXGL.getDialogService().showMessageBox("Game Over - Todos los jugadores han muerto",
                () -> FXGL.getGameController().gotoMainMenu());
    }

    // Mostrar solo el HUD del jugador actual
    private void showOnlyHUDOf(PlayerEntity player) {
        for (PlayerHUD hud : huds) {
            hud.setVisible(hud.playerEntity == player);
        }
    }

    // Ocultar todos los HUDs
    private void hideAllHUDs() {
        for (PlayerHUD hud : huds) {
            hud.setVisible(false);
        }
    }

    // Actualiza todos los HUDs (vida, habilidad, etc.)
    public void updateAllHUDs() {
        for (PlayerHUD hud : huds) {
            hud.updateHUD();
        }
    }

    public void removeEnemy(EnemyEntity enemy) {
        enemies.remove(enemy);
        ENEMY_LOGIC_MAP.remove(enemy.getEntity());
    }
}
