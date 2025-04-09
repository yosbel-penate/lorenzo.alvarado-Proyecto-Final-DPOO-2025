package enemies;
import characters.character;
public class CombatDrones extends enemies implements SpecialAbility {

    public CombatDrones(String name, int RangeMovement, int RangeDamage, String SpecialAbility, int life, int FrequencySpecialAbility) {
        super(name, RangeMovement, RangeDamage, SpecialAbility, life, FrequencySpecialAbility);
    }
    public void setAttack() {
        if (aleatorio>5){
            damage=2;
        }else {
            damage=3;
        }
    }





    public void  SetSpecialAbility(character aim) {// si hay enemigos en un radio de 5 casillas este va para arriba del personaje
if(Distanc<=5){// si la distancia es menor q 5 activar habilidad especial en el rango ese
    if(DistancX>DistancY){// empezar a mover en la distancia mayor
        if(aim.getPositionXCharacter()>this.positionXenemies){
            this.positionXenemies+=velocity;}
            else{positionXenemies-=velocity;
        }
        if(aim.getPositionYCharacter()>this.postionYenemies){
            this.postionYenemies+=velocity;}
        else{postionYenemies-=velocity;
        }
    }// el enemigo caminara para arriba del personaje ver cuantas casillas y comoo lo hace
      }
    }
}
