package enemies;

import characters.character;

public class AraniaCosmica extends enemies implements SpecialAbility {
    public AraniaCosmica(String name, int RangeMovement, int RangeDamage, String SpecialAbility, int life, int FrequencySpecialAbility) {
        super(name, RangeMovement, RangeDamage, SpecialAbility, life, FrequencySpecialAbility);
    }

    public void setAttack(){
        if (aleatorio>4){
            damage=3;
        }else {
            damage=2;
        }

    }
    public void SetSpecialAbility(character aim){
        if(Distanc<=3){
       do {
           aim.isMovements(false);
       }while (GetLif()==0);
            };
        }
    }



