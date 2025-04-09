
package com.example.galactic.characters;
import com.example.galactic.enemies.*;
import com.example.galactic.MagicObjet.*;
public class character {
    int aleatorio = (int) Math.random() * 10;
  // movimientos
    int positionXCharacter;
    int positionYCharacter;
    int Distance;
    boolean movements = true;
   // Caracteristicas
    String name;
    int LifeCharacter;
    int danio;


    int DamageEnemies=0;


    public character(String name, int danio, int LifeCharacter) {
        this.danio = danio;
        this.LifeCharacter = LifeCharacter;
        this.name = name;
    }

    public void attack(enemies enemies) {
        System.out.println(name + " ataca a " + enemies.getName());
        enemies.TakeDamage(danio);
    }

    public int getLifeCharacter() {
        return this.LifeCharacter;
    }

    public String getName() {
        return name;
    }

    public void TakeDamage(int DamageEnemies) {
        this.DamageEnemies=DamageEnemies;
        LifeCharacter -= DamageEnemies;

        if (LifeCharacter < 0) {
            LifeCharacter = 0;
        }
    }
    public int GetTakeDamage(){
        return DamageEnemies;
    }


    public void SetPositionCharacter(int positionX, int positionY) {
        this.positionXCharacter = positionX;
        this.positionYCharacter = positionY;
        Distance = positionYCharacter + positionXCharacter;
    }

    public int getPositionXCharacter() {
        return positionXCharacter;
    }

    public int getPositionYCharacter() {
        return positionYCharacter;
    }


    public int Distance() {
        return Distance;
    }

    public void isMovements(boolean movements) {
        this.movements = movements;
    }



public int GetLifePotionInt(PotionLife potion){//para que el enemigo pirata pueda robar

        return potion.GetLifePotionInt();
}
    public int GetEnergyShielInt(EnergyShield Shield){//para poder usarlo en el enemigo pirata q roba un objeto
        return Shield.GetEnergyShielInt();
    }


    public int GetPlasmaPumpInt(PlasmaPump pump){// para poder usarlo en el enemigo pirata q roba un objeto
        return pump.GetPlasmaPumpInt();
    }
    // bomba de plasma tiene un rango de danio de 3 casillas, pero lo lanza en un rango de 5 casillas es decir q puede caer en un radio de 5 casilla y explotar a 3, no hace danio a los personajes














}




