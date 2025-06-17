package Domain.Levels;

import Domain.Entity.Characters.Enemies.*;
import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.Characters.Players.PlayerEntity;
import Domain.Entity.Combat.TurnManager;
import Domain.Entity.Elements.ObjetosMagicos.BombaPlasma;
import Domain.Entity.Elements.ObjetosMagicos.EscudoEnergia;
import Domain.Entity.Elements.ObjetosMagicos.PocionCurativa;
import Domain.Entity.Elements.Trampas.AgujeroNegro;
import Domain.Entity.Elements.Trampas.NebulosaToxica;
import Domain.Entity.Elements.Trampas.TrampaAntigua;
import Domain.Entity.EntityType;
import View.Maps.MapLoader;
import View.UI.Menu.WinScreen;
import View.UI.PlayerHUD;
import View.UI.Puzzle.MirrorMazePuzzleFX;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Level2 {
    private MapLoader mapLoader;
    private List<PlayerEntity> playerEntities= new ArrayList<>();
    private TurnManager turnManager;
    private List<EnemyEntity> enemies = new ArrayList<>();
    private PlayerHUD playerHUD;
    private List<Entity> itemsOnMap = new ArrayList<>();
    private List<AgujeroNegro> agujerosNegros = new ArrayList<>();
    private List<NebulosaToxica> nebulosasToxicas = new ArrayList<>();
    private List<TrampaAntigua> trampasAntiguas = new ArrayList<>();
    private MirrorMazePuzzleFX mirrorMazePuzzle;
    private WinScreen winScreen;

    private List<Hero> getHeroesFromPlayers() {
        List<Hero> heroes = new ArrayList<>();
        for (PlayerEntity player : playerEntities) {
            heroes.add(player.getHero());
        }
        if (playerEntities == null) {
            System.err.println("playerEntities no ha sido inicializado.");
        }   return heroes;

    }

    public void initialize(List<String> selectedCharacters) {
        FXGL.getGameWorld().removeEntities();
        FXGL.getGameScene().clearGameViews();
        FXGL.getGameScene().clearUINodes();

        mapLoader = new MapLoader();
        mapLoader.loadMap("Map2.tmx");
        setupPuzzleKeyListener();
        turnManager = new TurnManager();
        playerEntities = new ArrayList<>();
        int startX = 5;
        int startY = 15;

        for (int i = 0; i < selectedCharacters.size(); i++) {
            int offsetX = 1 - (i % 3);
            int offsetY = i / 3;

            PlayerEntity playerEntity = new PlayerEntity(
                    selectedCharacters.get(i),
                    startX + offsetX,
                    startY + offsetY
            );

            playerEntity.setTurnManager(turnManager);
            playerEntity.spawnPlayer();
            playerEntity.getHero().setPlayerEntity(playerEntity);
            playerEntities.add(playerEntity);
            turnManager.addPlayer(playerEntity);
            if (i == 0) {
                PlayerEntity.setCurrentHero(playerEntity.getHero());
            }
        }

        if (!playerEntities.isEmpty()) {
            PlayerEntity firstPlayer = playerEntities.get(0);

            playerHUD = new PlayerHUD(firstPlayer, 0);
            firstPlayer.setHUD(playerHUD);
            turnManager.addHUD(playerHUD);
            FXGL.addUINode(playerHUD.getRoot());

            // Iniciamos el turno del primer jugador
            firstPlayer.startTurn();

            // Agregamos un delay para centrar la cámara sobre el primer jugador
            FXGL.getGameTimer().runOnceAfter(() -> {
                System.out.println("Centrando cámara para primer jugador: x=" + firstPlayer.getEntity().getX() +
                        ", y=" + firstPlayer.getEntity().getY());
                turnManager.focusCameraOn(firstPlayer.getEntity());
            }, Duration.seconds(0.2));
        }


        for (int i = 1; i < playerEntities.size(); i++) {
            PlayerEntity player = playerEntities.get(i);
            PlayerHUD hud = new PlayerHUD(player, i);
            player.setHUD(hud);
            turnManager.addHUD(hud);
            FXGL.addUINode(hud.getRoot()); // ✅ Esto agrega el HUD correspondiente a cada jugador
        }


        FXGL.getGameScene().getRoot().getStylesheets().add("assets/ui/hud-styles.css");

        spawnEnemies();
        spawnItems();
        spawnTraps();
        FXGL.getGameTimer().runAtInterval(() -> {
            updateTraps();
        }, Duration.seconds(1)); // Se ejecuta cada 1 segundo (ajusta el tiempo si quieres)

    }


    private void setupPuzzleKeyListener() {
        Input input = FXGL.getInput();

        input.addAction(new UserAction("Abrir Acertijo") {
            @Override
            protected void onActionBegin() {
                MirrorMazePuzzleFX puzzle = new MirrorMazePuzzleFX(() -> {
                    System.out.println("¡Acertijo completado!");

                    // Obtén el stage principal de FXGL
                    Stage stage = FXGL.getPrimaryStage();

                    if (winScreen == null) {
                        winScreen = new WinScreen(stage);
                    }
                    winScreen.show();
                });
                puzzle.show();
            }
        }, KeyCode.L);
    }


    private Point2D findValidSpawnPosition(Set<Point2D> usedPositions) {
        Point2D spawnPos;

        do {
            spawnPos = findFreeSpawnPosition(usedPositions);
        } while (isBlocked((int) spawnPos.getX(), (int) spawnPos.getY()));

        return spawnPos;
    }

    private void spawnEnemies() {
        enemies.clear();
        Set<Point2D> usedPositions = new HashSet<>();

        for (int i = 0; i < 6; i++) {
            Point2D spawnPos = findValidSpawnPosition(usedPositions);

            GiganteLava giganteLogic = new GiganteLava();
            EnemyEntity gigante = new EnemyEntity(giganteLogic, playerEntities, turnManager);

            Entity fxglEntity = FXGL.spawn("enemy",
                    new SpawnData((int) spawnPos.getX(), (int) spawnPos.getY())
                            .put("type", "gigante")
                            .put("logic", giganteLogic)
            );

            gigante.setEntity(fxglEntity);
            gigante.getLogic().setPosition((int) fxglEntity.getX() / 56, (int) fxglEntity.getY() / 56);

            enemies.add(gigante);
            turnManager.addEnemy(gigante);
            usedPositions.add(spawnPos);
        }

        for (int i = 0; i < 5; i++) {
            Point2D spawnPos = findValidSpawnPosition(usedPositions);

            PirataEspacial pirataLogic = new PirataEspacial();
            EnemyEntity pirata = new EnemyEntity(pirataLogic, playerEntities, turnManager);

            Entity fxglEntity = FXGL.spawn("enemy",
                    new SpawnData((int) spawnPos.getX(), (int) spawnPos.getY())
                            .put("type", "pirata")
                            .put("logic", pirataLogic)
            );

            pirata.setEntity(fxglEntity);
            pirata.getLogic().setPosition((int) fxglEntity.getX() / 56, (int) fxglEntity.getY() / 56);

            enemies.add(pirata);
            turnManager.addEnemy(pirata);
            usedPositions.add(spawnPos);

            System.out.println("Spawned AranaCosmica at (" + (int) spawnPos.getX() + ", " + (int) spawnPos.getY() + ")");
        }
    }


    private Point2D findFreeSpawnPosition(Set<Point2D> usedPositions) {
        int minX = 25, maxX = 56;
        int minY = 10, maxY = 20;
        int maxAttempts = 100;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = FXGL.random(minX, maxX);
            int y = FXGL.random(minY, maxY);
            Point2D pos = new Point2D(x, y);

            boolean occupied = FXGL.getGameWorld()
                    .getEntitiesByType(EntityType.OBSTACULOS)
                    .stream()
                    .anyMatch(e -> (int) (e.getX() / 56) == x && (int) (e.getY() / 56) == y);

            if (!usedPositions.contains(pos) && !occupied) {
                return pos;
            }
        }

        // Si no encuentra posición libre, devuelve una por defecto
        return new Point2D(minX, minY);
    }

    public void damageEnemy(EnemyEntity enemy, int damage) {
        enemy.getLogic().takeDamage(damage);
        enemy.showDamage(damage);
    }

    public PlayerHUD getPlayerHUD() {
        return playerHUD;
    }

    public List<PlayerEntity> getPlayerEntities() {
        return playerEntities;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    private void spawnItems() {
        itemsOnMap.clear();
        Set<Point2D> usedPositions = new HashSet<>();

        // Evitar que ítems se coloquen sobre enemigos, jugadores u obstáculos
        for (Entity e : FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY, EntityType.PLAYER, EntityType.OBSTACULOS)) {
            int tileX = (int) (e.getX() / 56);
            int tileY = (int) (e.getY() / 56);
            usedPositions.add(new Point2D(tileX, tileY));
        }

        // Spawn 3 pociones
        for (int i = 0; i < 3; i++) {
            Point2D pos = findFreeSpawnPosition(usedPositions);
            PocionCurativa pocion = new PocionCurativa();

            Entity itemEntity = FXGL.spawn("item",
                    new SpawnData((int) pos.getX() + 28, (int) pos.getY() + 28)
                            .put("item", pocion)
            );

            itemsOnMap.add(itemEntity);
            usedPositions.add(pos);
        }

        // Spawn 2 escudos
        for (int i = 0; i < 2; i++) {
            Point2D pos = findFreeSpawnPosition(usedPositions);
            EscudoEnergia escudo = new EscudoEnergia();

            Entity itemEntity = FXGL.spawn("item",
                    new SpawnData((int) pos.getX(), (int) pos.getY())
                            .put("item", escudo)
            );

            itemsOnMap.add(itemEntity);
            usedPositions.add(pos);
        }
        for(int i=0;i<3;i++) {
            Point2D pos = findFreeSpawnPosition(usedPositions);
            BombaPlasma bomba = new BombaPlasma();

            Entity itemEntity = FXGL.spawn("item",
                    new SpawnData((int) pos.getX(), (int) pos.getY())
                            .put("item", bomba)
            );

            itemsOnMap.add(itemEntity);
            usedPositions.add(pos);
        }
    }

    private void spawnTraps() {
        // Agujero Negro
        AgujeroNegro agujero = new AgujeroNegro();
        agujero.setPosition(30, 21);
        agujerosNegros.add(agujero);

        FXGL.spawn("trampa", new SpawnData(30, 21)
                .put("elemento", agujero)
                .put("spriteName", "blackhole.png"));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                NebulosaToxica nebulosa = new NebulosaToxica();

                // Define la posición de cada nebulosa, por ejemplo, en fila o en un patrón
                int x = 20 + i;  // ejemplo: x de 20 a 28
                int y = 14+j;      // misma fila para todas
                nebulosa.setPosition(x, y);

                nebulosasToxicas.add(nebulosa);

                FXGL.spawn("trampa", new SpawnData(x, y)
                        .put("elemento", nebulosa)
                        .put("spriteName", "nebulosa.png"));

                // Trampa Antigua
                TrampaAntigua trampa = new TrampaAntigua();
                trampa.setPosition(12, 12);
                trampasAntiguas.add(trampa);

                FXGL.spawn("trampa", new SpawnData(12, 12)
                        .put("elemento", trampa)
                        .put("spriteName", "trampa.png"));
            }
        }
    }
    private List<Point2D> generateNearbyPositions(Point2D center, int radius, Set<Point2D> used) {
        List<Point2D> positions = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx == 0 && dy == 0) continue;

                Point2D pos = new Point2D(center.getX() + dx, center.getY() + dy);
                boolean occupied = FXGL.getGameWorld()
                        .getEntitiesByType(EntityType.OBSTACULOS)
                        .stream()
                        .anyMatch(e -> (int)(e.getX() / 56) == pos.getX() && (int)(e.getY() / 56) == pos.getY());

                if (!used.contains(pos) && !occupied) {
                    positions.add(pos);
                }
            }
        }

        return positions;
    }


    public void updateTraps() {
        List<Hero> heroes = getHeroesFromPlayers();

        for (AgujeroNegro agujero : agujerosNegros) {
            agujero.applyEffect(heroes);
        }
        for (NebulosaToxica nebulosa : nebulosasToxicas) {
            nebulosa.applyEffect(heroes);
        }
        for (TrampaAntigua trampa : trampasAntiguas) {
            trampa.applyEffect(heroes); }
    }
    public boolean isBlocked(int tileX, int tileY) {
        return FXGL.getGameWorld()
                .getEntitiesByType(EntityType.OBSTACULOS)
                .stream()
                .anyMatch(e -> {
                    int ex = (int) (Math.round(e.getX() / 56));
                    int ey = (int) (Math.round(e.getY() / 56));
                    return ex == tileX && ey == tileY;
                });
    }

    public void nextLevel() {

    }
}
