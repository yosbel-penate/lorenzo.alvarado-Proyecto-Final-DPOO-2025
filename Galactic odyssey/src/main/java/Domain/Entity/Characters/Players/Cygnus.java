package Domain.Entity.Characters.Players;

public class Cygnus extends Hero {

    private boolean evasiveMode = false;
    private int evasiveTurns = 0;
    private int originalMoveRange;

    public Cygnus() {
        super("Cygnus", 16, 7, 3, 2, 3, 3);
        this.originalMoveRange = getMoveRange();  // Guardar el rango original de movimiento
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("Maniobra Evasiva activada: esquiva el próximo ataque y aumenta velocidad por 2 turnos.");
        evasiveMode = true;
        evasiveTurns = 2;
        setMoveRange(originalMoveRange + 2);  // Aumentar el rango de movimiento
        resetCooldown();
    }

    @Override
    public void onNewTurn() {
        super.onNewTurn();

        if (evasiveTurns > 0) {
            evasiveTurns--;
            if (evasiveTurns == 0) {
                evasiveMode = false;
                setMoveRange(originalMoveRange);  // Restaurar el rango original después de la habilidad
                System.out.println("Maniobra Evasiva terminó.");
            }
        }
    }

    public boolean isInEvasiveMode() {
        return evasiveMode;
    }


    public void receiveDamage(int damage) {
        if (evasiveMode) {
            System.out.println(getName() + " esquivó el ataque gracias a Maniobra Evasiva.");
        } else {
            super.receiveDamage(damage);  // Llamar al método de la clase padre para recibir daño
        }
    }
}
