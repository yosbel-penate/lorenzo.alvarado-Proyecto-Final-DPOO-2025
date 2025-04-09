package com.example.galactic.MagicObjet;

import com.example.galactic.characters.character;
import com.example.galactic.enemies.enemies;

public class PlasmaPump extends Objet{
    public PlasmaPump(String name,int RangoUso){

        super();
    }

    public void TakePlasmaPump(character character) {
        // a personajes q esten en el rango de 1 casilla
if(MagicItemCounter==0){
        MagicItemCounter = +1;}

        PlasmaPumpCounter = +1;
        System.out.println(" Ha conseguido una Bomba de Plasma ");
    }
    public void movementPump(character je){// movimiento de la bomba, desde la posicion inicial
        PositionPlasmaPumpX=je.getPositionXCharacter();
        PositionPlasmaPumpY=je.getPositionYCharacter();

    }


    public int getMovementPlasmaPumpX() {
        return PositionPlasmaPumpX;
    }

    public int getPositionPlasmaPumpY() {
        return PositionPlasmaPumpY;
    }

    public void ActivatePlasmaPump(enemies enemies, character character) {// pa donde este virado el personaje seria esta funcion buscar despues como hacer eso de estar virado
       // rango de movimiento de la bomba
        int Distancia=enemies.Distanc;
        int DistanciaX=enemies.DistancX;
        int DistanciaY= enemies.DistancY;
        //Posicion en donde esta la bomba
        PositionPlasmaPumpY=character.getPositionXCharacter();
        PositionPlasmaPumpX=character.getPositionYCharacter();
        // distancia del objeto al enemigo
        int DistanciaObjetoEnemigoX=Math.abs(PositionPlasmaPumpX-enemies.positionXenemies);
        int DistanciaObjetoEnemigoY=Math.abs(PositionPlasmaPumpY-enemies.postionYenemies);
        int DistanciaObjetoEnemigo=Math.abs(DistanciaObjetoEnemigoY+DistanciaObjetoEnemigoX);

        if (Distancia<=8){//la distacnia es 8 xq incluye el rango de la explocion
            while (Distancia<=5){// rango de movimiento
                if (DistanciaX!=0){
                    PositionPlasmaPumpY++;
                }
                if(DistanciaY!=0){
                    PositionPlasmaPumpX++;
                }}
        }else{
            System.out.println("No usar, rango de movimiento invalido");

        }


        if (DistanciaObjetoEnemigo <= 3) {
            if (aleatorio < 3) {
                enemies.TakeDamage(4);
            } else if (aleatorio < 7) {
                enemies.TakeDamage(5);
            } else {
                enemies.TakeDamage(6);
            }
            if (MagicItemCounter==0){
                MagicItemCounter=-1;
            }
            PlasmaPumpCounter=-1;
        }
    }


    public String GetPlasmaPump(){
        return "Cantidad  Bombas de Plasma: "+PlasmaPumpCounter;
    }
    public int GetPlasmaPumpInt(){// para poder usarlo en el enemigo pirata q roba un objeto
        return PlasmaPumpCounter;
    }
    // bomba de plasma tiene un rango de danio de 3 casillas, pero lo lanza en un rango de 5 casillas es decir q puede caer en un radio de 5 casilla y explotar a 3, no hace danio a los personajes
int PositionPlasmaPumpX;
    int PositionPlasmaPumpY;

}
