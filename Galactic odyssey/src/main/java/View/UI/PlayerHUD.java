package View.UI;

import Domain.Entity.Characters.Players.PlayerEntity;
import Domain.Entity.Characters.Players.Hero;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class PlayerHUD {
    public PlayerEntity playerEntity;
    private Text lifeValueText;
    private Text specialAbilityStatusText;
    private Text specialAbilityCooldownText;
    private StackPane lifeBar;
    private StackPane lifeBarContainer;
    private VBox specialAbilityContainer;
    private VBox hudContainer;

    public PlayerHUD(PlayerEntity playerEntity, int index) {
        this.playerEntity = playerEntity;
        initHUD(index);
    }

    private void initHUD(int index) {
        hudContainer = new VBox(10);
        hudContainer.setId("hud-container");

        // Posición dinámica para cada jugador (vertical stacking)
        hudContainer.setTranslateX(20);
        hudContainer.setTranslateY(20 + index * 100);

        lifeBarContainer = new StackPane();
        lifeBarContainer.getStyleClass().add("life-bar-container");
        lifeBarContainer.setPrefSize(300, 30);

        lifeBar = new StackPane();
        lifeBar.getStyleClass().add("life-bar");

        Pane galaxyEffect = new Pane();
        galaxyEffect.getStyleClass().add("life-bar-galaxy-effect");
        galaxyEffect.setPrefSize(300, 30);

        Text lifeText = new Text("BARRA DE VIDA GALAXIA");
        lifeText.setId("life");
        StackPane.setAlignment(lifeText, javafx.geometry.Pos.TOP_LEFT);
        StackPane.setMargin(lifeText, new javafx.geometry.Insets(2, 5, 0, 0));

        lifeValueText = new Text();
        lifeValueText.setId("life");
        StackPane.setAlignment(lifeValueText, javafx.geometry.Pos.TOP_RIGHT);
        StackPane.setMargin(lifeValueText, new javafx.geometry.Insets(2, 5, 0, 0));

        lifeBarContainer.getChildren().addAll(lifeBar, galaxyEffect, lifeText, lifeValueText);

        specialAbilityContainer = new VBox(5);
        specialAbilityContainer.getStyleClass().add("special-ability-container");

        specialAbilityStatusText = new Text("HABILIDAD ESPECIAL");
        specialAbilityStatusText.setId("specialAbility");

        specialAbilityCooldownText = new Text("RECARGA: 0 TURNOS");
        specialAbilityCooldownText.setId("cooldown");

        specialAbilityContainer.getChildren().addAll(specialAbilityStatusText, specialAbilityCooldownText);

        hudContainer.getChildren().addAll(lifeBarContainer, specialAbilityContainer);
        FXGL.getGameScene().addUINode(hudContainer);
    }

    public void updateHUD() {
        if (playerEntity == null || playerEntity.getHero() == null) {
            setDeadState();
            return;
        }

        Hero hero = playerEntity.getHero();
        updateHealthBar(hero);
        updateSpecialAbility(hero);
    }

    private void updateHealthBar(Hero hero) {
        int currentHealth = Math.max(0, hero.health);
        int maxHealth = getMaxHealth(hero);

        double healthPercentage = (maxHealth > 0) ? (double) currentHealth / maxHealth : 0;
        healthPercentage = Math.max(0, Math.min(1, healthPercentage));

        lifeBar.setPrefWidth(300 * healthPercentage);
        lifeValueText.setText(currentHealth + "/" + maxHealth);
    }

    private void updateSpecialAbility(Hero hero) {
        String status = hero.canUseSpecial() ? "DISPONIBLE" : "EN RECARGA";
        specialAbilityStatusText.setText("HABILIDAD: " + status);

        int cooldown = Math.max(0, hero.currentCooldown);
        specialAbilityCooldownText.setText("RECARGA: " + cooldown + " TURNOS");
    }

    private void setDeadState() {
        lifeBar.setPrefWidth(0);
        lifeValueText.setText("0/0");
        specialAbilityStatusText.setText("HABILIDAD: INACTIVA");
        specialAbilityCooldownText.setText("JUGADOR DERROTADO");
    }

    private int getMaxHealth(Hero hero) {
        return 20; // Puedes cambiar esto si tienes hero.maxHealth
    }
    public void setVisible(boolean visible) {
        hudContainer.setVisible(visible);
    }
}
