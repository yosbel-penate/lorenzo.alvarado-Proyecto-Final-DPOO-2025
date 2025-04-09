package com.example.galactic.MagicObjet;

import com.example.galactic.characters.character;

public class GasMask extends Objet{
    public GasMask(String name,int RangoUso){

        super();
    }


    public void TakeGasMask(character character) {
        // deja sin efecto a la nebulosa Toxica

        MagicItemCounter = +1;
        GasMaskCounter = +1;
        System.out.println(" Ha conseguido una Mascara Antigas ");
    }
    public void ActivateGasMask( character character){
        boolean DanioNebulosa=false; // poner q cuando se active la nebulosa sea true, para q caundo se ponga false se quede sin aplicacion

    }
    public String GetGAsMask(){
        return "Cantidad de MascarasAntigas: "+        GasMaskCounter;
    }

}
