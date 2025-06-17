package Domain.Entity.Characters.Players;

import Domain.Entity.Characters.Enemies.Enemy;
import Domain.Entity.Elements.ObjetosMagicos.Item;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import java.util.*;
public abstract class Hero extends Component {

    protected int intelligence = 0;
    private Map<HeroStatusEffect, Integer> activeEffects = new HashMap<>();
    private List<Item> inventory = new ArrayList<>();
    public String name;
    public int health;
    public int moveRange;
    public int attackRange;
    public int minDamage;
    public int maxDamage;
    protected int specialCooldown;
    public int currentCooldown;
    protected int x, y;
    protected int armor;
    private int shield = 0;
    private boolean hasProtection = false;
    private List<HeroStatusEffect> statusEffects;
    protected List<Enemy> enemies;
    private String textureName;

    private PlayerEntity playerEntity;
    public Hero(String name, int health, int moveRange, int attackRange,
                int minDamage, int maxDamage, int specialCooldown, String textureName) {
        this.name = name;
        this.health = health;
        this.moveRange = moveRange;
        this.attackRange = attackRange;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.specialCooldown = specialCooldown;
        this.currentCooldown = 0;
        this.statusEffects = new ArrayList<>();
        this.textureName= textureName;
    }

    public abstract void useSpecialAbility();

    public Hero(List<Enemy> enemies) {
        this.enemies = enemies;
    }
    public void moveTo(int targetX, int targetY) {
        setPosition(targetX, targetY);  // Asumiendo que setPosition es un método accesible
    }

    public void addShield(int amount) {
        shield += amount;
        System.out.println(name + " recibió un escudo de " + amount + " puntos.");
    }
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    private void moveHeroToward(Hero hero, int targetX, int targetY) {
        // Calcular la diferencia en X y Y
        double dx = targetX - hero.getX();
        double dy = targetY - hero.getY();

        // Calcular la distancia entre el héroe y el objetivo
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Si la distancia es muy pequeña (umbral para evitar movimientos minúsculos)
        if (distance < 1.0) {
            System.out.println(hero.getName() + " ya está en la posición objetivo.");
            return; // No mover si ya estamos cerca o en la misma casilla
        }

        // Normalizar la dirección (hacer que el vector de dirección sea unidad)
        dx /= distance;
        dy /= distance;

        // Velocidad de movimiento (ajustar según la rapidez que desees)
        double speed = 2.0;

        // Calcular las nuevas coordenadas
        double newX = hero.getX() + dx * speed;
        double newY = hero.getY() + dy * speed;

        // Mostrar las coordenadas antes de mover
        System.out.println(hero.getName() + " se mueve de (" + hero.getX() + ", " + hero.getY() + ") a (" + newX + ", " + newY + ")");

        // Establecer la nueva posición del héroe
        hero.setPosition((int)newX, (int)newY);

        // Verificar que el héroe se ha movido
        System.out.println(hero.getName() + " se ha movido a: (" + hero.getX() + ", " + hero.getY() + ")");
    }




    public void setPlayerEntity(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }
    public boolean hasProtection() {
        return hasProtection;
    }

    public void setProtection(boolean value) {
        this.hasProtection = value;
        System.out.println(name + (value ? " ahora tiene protección contra efectos tóxicos." : " ha perdido su protección."));
    }

    public void takeDamage(int amount) {
        receiveDamage(amount);
        System.out.println(name + " recibió " + amount + " puntos de daño.");
    }

    public Enemy findWeakestEnemy() {
        if (enemies == null || enemies.isEmpty()) {
            return null;
        }
        Enemy weakestEnemy = enemies.get(0);
        for (Enemy enemy : enemies) {
            if (enemy.getHealth() < weakestEnemy.getHealth()) {
                weakestEnemy = enemy;
            }
        }
        return weakestEnemy;
    }

    public void restoreHealth(int amount) {
        this.health += amount;
        System.out.println(name + " ha recuperado " + amount + " puntos de salud.");
    }

    private boolean hidden;

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void removeStatusEffect(HeroStatusEffect effect) {
        if (statusEffects.contains(effect)) {
            statusEffects.remove(effect);
            System.out.println("Efecto de estado eliminado: " + effect);
        }
    }

    public boolean hasStatusEffect(HeroStatusEffect effect) {
        return statusEffects.contains(effect);
    }

    public void resetCooldown() {
        currentCooldown = specialCooldown;
    }

    public void reduceCooldown() {
        if (currentCooldown > 0) currentCooldown--;
    }

    public boolean canUseSpecial() {
        return currentCooldown == 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getDistanceTo(int tx, int ty) {
        return Math.abs(tx - x) + Math.abs(ty - y);
    }

    public void moveToward(int tx, int ty) {
        if (playerEntity == null) {
            System.err.println("playerEntity es null en Hero.moveToward");
            return;
        }
        int nextX = x;
        int nextY = y;

        if (x < tx) nextX++;
        else if (x > tx) nextX--;
        else if (y < ty) nextY++;
        else if (y > ty) nextY--;

        if (!playerEntity.isTileOccupied(nextX, nextY)) {
            x = nextX;
            y = nextY;
            System.out.println(name + " se mueve a (" + x + ", " + y + ")");
        } else {
            System.out.println("Casilla ocupada en (" + nextX + ", " + nextY + "), " + name + " no se mueve.");
        }

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

    public int getMoveRange() {
        return moveRange;
    }

    public void setMoveRange(int moveRange) {
        this.moveRange = moveRange;
    }

    public String getName() {
        return name;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void increaseIntelligence(int amount) {
        intelligence += amount;
        System.out.println(getName() + " ha aumentado su inteligencia a " + intelligence);
    }

    public void receiveDamage(int damage) {
        if (shield > 0) {
            int absorbed = Math.min(damage, shield);
            damage -= absorbed;
            shield -= absorbed;
            System.out.println(name + " absorbió " + absorbed + " puntos de daño con escudo.");
        }
        health -= damage;
        if (health < 0) health = 0;
    }

    public void restoreArmor(int amount) {
        this.armor += amount;
        System.out.println(name + " ha restaurado " + amount + " puntos de armadura. Armadura total: " + this.armor);
    }

    public void onNewTurn() {
        for (HeroStatusEffect effect : statusEffects) {
            switch (effect) {
                case STUNNED:
                    System.out.println(name + " está aturdido y no puede moverse.");
                    break;
                case SHIELDED:
                    System.out.println(name + " está protegido por un escudo.");
                    break;
                case HIDDEN:
                    System.out.println(name + " está oculto.");
                    break;
                default:
                    break;
            }
        }
    }

    public void addStatusEffect(HeroStatusEffect effect) {
        addStatusEffect(effect, 1);
    }

    public void addStatusEffect(HeroStatusEffect effect, int duration) {
        activeEffects.put(effect, duration);
        System.out.println(name + " ha sido afectado por " + effect + " durante " + duration + " turno(s).");
    }

    public void updateEffects() {
        Iterator<Map.Entry<HeroStatusEffect, Integer>> iterator = activeEffects.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<HeroStatusEffect, Integer> entry = iterator.next();
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                System.out.println(name + " ya no está afectado por " + entry.getKey() + ".");
                iterator.remove();
            } else {
                entry.setValue(remaining);
            }
        }
    }
    public int getVelocity(){
        return 1;
    }
    public boolean hasEffect(HeroStatusEffect effect) {
        return activeEffects.containsKey(effect);
    }
    public void heal(int amount) {
        restoreHealth(amount); // Esto llamará al método existente 'restoreHealth'
    }
    public void addItemToInventory(Item item) {
        inventory.add(item);
        System.out.println(name + " recogió: " + item.getName());
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public String getTextureName() {
        return textureName;
    }
}