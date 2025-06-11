package Domain.Entity.Characters.Enemies;

import com.almasb.fxgl.entity.component.Component;

public class EnemyComponent extends Component {
    private Enemy enemy;

    public EnemyComponent() {
        // Constructor vacío requerido por FXGL
    }
    public void onUpdate(double tpf) {
        // Aquí puedes actualizar el estado, cooldown, etc.
    }
    public EnemyComponent(Enemy enemy) {
        this.enemy = enemy;
    }

    public void takeDamage(int damage) {
        if (enemy != null) {
            enemy.takeDamage(damage);
        }
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
}
