package Domain.Entity.Characters.Players;

import java.util.List;

public class CapOrion extends Hero {
    private int turnCounter;
    private int specialEffectDuration;
    private List<PlayerEntity> allies;

    public CapOrion() {
        super("Capitán Orion", 20, 5, 3, 2, 4, 3);
        this.turnCounter = 0;
        this.specialEffectDuration = 0;
    }

    @Override
    public void useSpecialAbility() {
        if (turnCounter >= 3) {
            if (specialEffectDuration == 0) {
                System.out.println("Inspiración Galáctica activada!");
                applySpecialEffect();
                specialEffectDuration = 2;
                turnCounter = 0;
            } else {
                System.out.println("La habilidad ya está activa.");
            }
        } else {
            System.out.println("Recarga: " + (3 - turnCounter) + " turnos restantes");
        }
    }

    private void applySpecialEffect() {
        if (allies != null) {
            allies.forEach(ally -> {
                if (getDistanceTo(ally) <= 4) {
                    ally.getHero().maxDamage += 1; // Aumenta ataque
                    ally.getHero().health += 1;    // Aumenta defensa
                }
            });
        }
    }

    private void revertSpecialEffect() {
        if (allies != null) {
            allies.forEach(ally -> {
                if (getDistanceTo(ally) <= 4) {
                    ally.getHero().maxDamage -= 1; // Revierte ataque
                    ally.getHero().health -= 1;   // Revierte defensa
                }
            });
        }
    }

    public void endSpecialEffectTurn() {
        if (specialEffectDuration > 0 && --specialEffectDuration == 0) {
            revertSpecialEffect();
        }
    }

    private int getDistanceTo(PlayerEntity ally) {
        return Math.abs(ally.getHero().getX() - getX()) +
                Math.abs(ally.getHero().getY() - getY());
    }

    public void incrementTurnCounter() {
        turnCounter++;
    }

    public void setAllies(List<PlayerEntity> allies) {
        this.allies = allies;
    }
}