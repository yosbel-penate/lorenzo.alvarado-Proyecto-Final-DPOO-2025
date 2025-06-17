package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.Characters.Enemies.EnemyComponent;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;
import java.util.stream.Collectors;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class Aurora extends Hero {
    public Aurora() {
        super("Aurora", 14, 6, 0, 0, 0, 3,"aurora.png");
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Negociación: evita combates con facciones enemigas en un radio de 6 casillas durante 2 turnos.");

        // Posición de Aurora
        int auroraX = getX();
        int auroraY = getY();

        // Buscar enemigos dentro del radio de 6 casillas
        getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemyEntity -> {
    
            EnemyComponent enemyComp = enemyEntity.getComponent(EnemyComponent.class);
            Enemy enemyLogic = enemyComp.getEnemy();

            int dx = Math.abs(enemyLogic.getX() - auroraX);
            int dy = Math.abs(enemyLogic.getY() - auroraY);

            if (dx + dy <= 6) {
                enemyLogic.disableAttackForTurns(2);
                System.out.println("- Enemigo " + enemyLogic.name + " ha sido afectado y no podrá atacar durante 2 turnos.");
            }
        });

        resetCooldown();
    }
}
