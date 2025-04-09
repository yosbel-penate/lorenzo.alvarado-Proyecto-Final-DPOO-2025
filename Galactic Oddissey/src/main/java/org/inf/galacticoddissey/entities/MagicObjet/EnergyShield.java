package com.example.galactic.MagicObjet;

import com.example.galactic.characters.character;

public class EnergyShield extends Objet {
    public EnergyShield(String name,int RangoUso){

        super();
    }
    public void TakeEnergyShield(character character) {
        // a personajes q esten en el rango de 1 casilla

        if(MagicItemCounter==0){
            MagicItemCounter = +1;}
        EnergyShielCounter = +1;
        System.out.println(" Ha conseguido un Escudo de Energia ");
    }

    public void ActivateEnergyShield(character character) {// pa poder activar esta habilidad especial debe tener al menos 1 escudo
        if(character.GetTakeDamage()<=4) {
            character.TakeDamage(0);
        }
        else {
            character.TakeDamage(character.GetTakeDamage()-4);
        }
        if(MagicItemCounter==0){
            MagicItemCounter = -1;}

        EnergyShielCounter = -1;

    }

    public String GetEnergyShield(){
        return "Cantidad de Escudo de Energia: "+EnergyShielCounter;
    }
    public int GetEnergyShielInt(){//para poder usarlo en el enemigo pirata q roba un objeto
        return EnergyShielCounter;
    }






}
