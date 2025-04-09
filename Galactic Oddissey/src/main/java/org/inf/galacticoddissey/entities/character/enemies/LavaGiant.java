package enemies;
import characters.character;
public class LavaGiant extends enemies implements SpecialAbility {
    public LavaGiant(String name, int RangeMovement, int RangeDamage, String SpecialAbility, int life, int FrequencySpecialAbility) {
        super(name, RangeMovement, RangeDamage, SpecialAbility, life, FrequencySpecialAbility);

    }
    public void setAttack(){
        if (aleatorio<3){
            damage=5;
        }else if (aleatorio<7){// serian 3 tipos de ataques habiendo mas posibilidades para q caiga el ataque de 2 y de 6 de danio
            damage=6;

        }else {
            damage=7;
        } }

    public void SetSpecialAbility(character aim ){
     if(Distanc<=3) {
            aim.TakeDamage(4);
        }}
    }






