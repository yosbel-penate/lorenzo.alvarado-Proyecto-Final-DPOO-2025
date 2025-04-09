package enemies;
import characters.character;

public class enemies   {
// establecer movimientos cada cierto tiempo con la interfaz time
   public int positionXenemies;
   public int postionYenemies;
   public int Distanc;
   public int DistancX;
   public int DistancY;
   public int velocity=1;
    private  String name;
private int RangeMovement;
private int RangeDamage;
protected int damage;

private String SpecialAbility;

    private int FrequencySpecialAbility;
    public int life;
        int aleatorio = (int) Math.random() * 10;// random para poner en cuantos turnos ataca el enemigo

    public void setAttack( character aim) {
     System.out.println(name+" ataca a "+ aim.getName());// esto seria q el personaje objetivo a atacar seria de la clase personaje y q tiene un nombre con sus atributos*/ );
   aim.TakeDamage(damage);
    }
public void TakeDamage(int damageCharacter){
        life-=damageCharacter;
    if (life<0){
        life=0;
    }
}



    public enemies(String name, int RangeMovement, int RangeDamage, String SpecialAbility, int life, int FrequencySpecialAbility){
    this.name=name;

    this.FrequencySpecialAbility=FrequencySpecialAbility;
    this.SpecialAbility=SpecialAbility;
    this.RangeDamage=RangeDamage;
    this.life=life;
}

    public int getDamage() {
      return   damage;
          // vida personaje va a ser igual a la misma vida menos el damage
    }
public String getName(){
        return name;
}
public int GetLif(){
        return life;
}

    public String getLife() {
        if (life>0){
        return " vida "+life;
    }else {
            return" enemigo eliminado";
        }}


    public int getFrequencySpecialAbility() {
        return FrequencySpecialAbility;
    }



    public int GetDistance() {
        return Distanc;
    }
    public void updateDistance(character character){
        int DistanceX=Math.abs(character.getPositionXCharacter()-positionXenemies);
        int DistanceY=Math.abs(character.getPositionYCharacter()-postionYenemies);
        int Distance=DistanceX+DistanceY;
        Distanc=Distance;
        DistancX=DistanceX;
        DistancY=DistanceY;
    }

}



