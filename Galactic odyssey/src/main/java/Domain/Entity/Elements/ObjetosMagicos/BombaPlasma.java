package Domain.Entity.Elements.ObjetosMagicos;

import Domain.Entity.Characters.Players.Hero;
import javafx.scene.image.Image;

public class BombaPlasma extends AbstractItem {
    public BombaPlasma(){
        super("assets/textures/BombaPlasma.png");
    }
    @Override
    public void use(Hero target) {
        // Se lanza la bomba a una posición
        System.out.println("Bomba de Plasma lanzada: explota en radio de 3 casillas, causando 4-6 de daño.");
        // Aplica daño a enemigos en área
    }
    public void setType(){
        type="BombaPlasma";
    }
    @Override
    public String getName() {
        return "Bomba de Plasma";
    }
    public String getType() {
        return type; // <- AQUÍ ESTABA FALTANDO
    }
    public Image getImage() {
        return super.getImage(); // usa la implementación de la clase base AbstractItem
    }
    }

