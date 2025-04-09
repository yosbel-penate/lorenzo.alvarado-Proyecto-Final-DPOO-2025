package com.example.galactic.MagicObjet;

public   class Objet {
  int element[]= new int[3];
  protected String name;
  protected int RangoUso;
    int aleatorio = (int) Math.random() * 10;
    //objetos
    int PotionLife;// da valor con random para rango de aumento de vida

    //Contadores en general



  int MagicItemCounter = 0;// Contador de objetos magicos
    int LifePotionCounter = 0;// Contador de pocion de vida
    int EnergyShielCounter = 0;// Contador de EScudod e energia
    int PlasmaPumpCounter=0;// Contador de bombda de plasma
    int GasMaskCounter=0;
  public Objet(){
        this.name=name;
        this.RangoUso=RangoUso;
    }
    public int GetMagicItemCounter(){
    return MagicItemCounter;
    }
}
