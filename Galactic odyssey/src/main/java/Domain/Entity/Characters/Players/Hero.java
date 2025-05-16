package Domain.Entity.Characters.Players;

public abstract class Hero {
    public String name;
    public int health;
    public int moveRange;
    public int attackRange;
    public int minDamage;
    public int maxDamage;
    protected int specialCooldown;
    public int currentCooldown;
    protected int x, y;

    public Hero(String name, int health, int moveRange, int attackRange,
                int minDamage, int maxDamage, int specialCooldown) {
        this.name = name;
        this.health = health;
        this.moveRange = moveRange;
        this.attackRange = attackRange;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.specialCooldown = specialCooldown;
        this.currentCooldown = 0;
    }

    public abstract void useSpecialAbility();

    public void resetCooldown() { currentCooldown = specialCooldown; }
    public void reduceCooldown() { if (currentCooldown > 0) currentCooldown--; }
    public boolean canUseSpecial() { return currentCooldown == 0; }
    public boolean isAlive() { return health > 0; }

    public int getDistanceTo(int tx, int ty) {
        return Math.abs(tx - x) + Math.abs(ty - y);
    }

    public void moveToward(int tx, int ty) {
        if (x < tx) x++;
        else if (x > tx) x--;
        else if (y < ty) y++;
        else if (y > ty) y--;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }

        System.out.println(name + " recibe " + amount + " de daño. Vida actual: " + health);
    }


    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }
}