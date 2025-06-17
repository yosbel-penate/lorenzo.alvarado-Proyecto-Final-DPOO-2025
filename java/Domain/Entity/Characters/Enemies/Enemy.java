package Domain.Entity.Characters.Enemies;

import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import java.util.List;
import View.Maps.GameUtils;
public abstract class Enemy {
    public String name;
    public int health;
    public int moveRange;
    public int attackRange;
    public int minDamage;
    public int maxDamage;
    protected int specialCooldown;
    protected int currentCooldown;
    protected int x, y;
    protected boolean active;
    private boolean controlled = false;  // Esta propiedad indica si el enemigo está controlado
    private boolean hidden;
    private boolean stunned = false; // Asegúrate de haberlo declarado


    // Nuevas propiedades para Aurora
    private int attackDisabledTurns = 0;

    public Enemy(String name, int health, int moveRange, int attackRange,
                 int minDamage, int maxDamage, int specialCooldown) {
        this.name = name;
        this.health = health;
        this.moveRange = moveRange;
        this.attackRange = attackRange;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.specialCooldown = specialCooldown;
        this.currentCooldown = 0;
        this.active = false;
    }

    public abstract void useSpecialAbility();

    public void resetCooldown() {
        currentCooldown = specialCooldown;
    }


    public boolean isControlled() {
        return controlled;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setControlled(boolean controlled) {
        this.controlled = controlled;
    }

    public String getName() {
        return name;
    }

    public void updateStatus() {
        // Aurora: reducir turnos sin atacar
        if (attackDisabledTurns > 0) {
            attackDisabledTurns--;
        }

        // Phobos: eliminar estado de aturdimiento tras un turno
        if (stunned) {
            stunned = false;
        }

        // Aquí podrías agregar otros efectos por turno si los hay
    }

    public boolean isStunned() {
        return stunned;
    }

    public void stun() {
        stunned = true;
    }

    public boolean canAttack() {
        return !stunned && attackDisabledTurns <= 0;
    }

    public void reduceCooldown() {
        if (currentCooldown > 0)
            currentCooldown--;
    }

    public boolean canUseSpecial() {
        return currentCooldown == 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getDistanceTo(int targetX, int targetY) {
        int dx = Math.abs(this.x - targetX);
        int dy = Math.abs(this.y - targetY);
        return dx + dy; // distancia Manhattan (ideal para grids)
    }


    public void moveToward(int tx, int ty) {
        int dx = Integer.compare(tx, x);
        int dy = Integer.compare(ty, y);

        int newX = x + dx;
        int newY = y + dy;

        // Intentar mover en X si no bloqueado
        if (dx != 0 && !GameUtils.isTileBlocked(newX, y)) {
            x = newX;
        }
        // Si no pudo mover en X, intentar en Y si no bloqueado
        else if (dy != 0 && !GameUtils.isTileBlocked(x, newY)) {
            y = newY;
        }
        // Si no pudo mover en ninguna dirección, se queda en su lugar (no se mueve)
    }


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void activate() {
        active = true;
    }

    public boolean isActive() {
        return active;
    }

    // ===== Aurora: métodos para bloquear ataques =====

    public void disableAttackForTurns(int turns) {
        attackDisabledTurns = turns;
    }

    public void takeDamage(int damage) {
        if (damage > 0) {
            this.health -= damage;
            System.out.println(name + " ha recibido " + damage + " puntos de daño.");
            if (!isAlive()) {
                System.out.println(name + " ha sido derrotado.");
            }
        }
    }

    public int getMaxHealth() {
        return health; // Si se necesita la salud máxima separada, puedes modificar esta lógica.
    }

    public int getHealth() {
        return health;
    }


    public int getAttackRange() {
        return attackRange;
    }
}
