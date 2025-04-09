package com.example.galactic.MagicObjet;

import com.example.galactic.characters.character;

public class PotionLife extends Objet {
    public PotionLife(String name,int RangoUso){

        super();
    }

    public void TakePotionLife(character character) {// a personajes q esten en el rango de 1 casilla

        MagicItemCounter = +1;
        LifePotionCounter = +1;
        System.out.println(" Ha conseguido una pocion de vida ");
    }

    public int GetRangePotionLife() {
        if (aleatorio < 2) {
            PotionLife = 3;
        } else if (aleatorio < 6) {
            PotionLife = 4;
        } else if (aleatorio < 9) {
            PotionLife = 5;
        }
        return PotionLife;
    }


    public int ActivateLifePotion(character characters) {
        // pa poder activar esta habilidad especial debe tener al menos 1 pocion
        return characters.getLifeCharacter() + PotionLife;

        // si tiene uno seleccionar al azar sino no hacer nd


        // pocion vida, armas plasma mascaras antigas
    }

    public String GetLifePotion() {
        return " Cantidad de Pocion de Vida: " + LifePotionCounter ;
    }
    public int GetLifePotionInt(){//para que el enemigo pirata pueda robar

        return PotionLife;
    }
}
