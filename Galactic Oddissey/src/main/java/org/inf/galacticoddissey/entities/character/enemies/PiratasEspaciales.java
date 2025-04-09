package enemies;

public class PiratasEspaciales extends enemies {
    public PiratasEspaciales(String name, int RangeMovement, int RangeDamage, String SpecialAbility, int life, int FrequencySpecialAbility) {
        super(name, RangeMovement, RangeDamage, SpecialAbility, life, FrequencySpecialAbility);
    }
    public void setAttack(){
        if (aleatorio<3){
            damage=2;
        }else if (aleatorio<5){// serian 4 tipos de ataques habiendo mas posibilidades para q caiga el ataque de 2 y de 4 de danio
            damage=3;
        } else if (aleatorio<8) {
            damage=4;
        }else {
            damage=5;
        }
    }




}
