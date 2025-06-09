package Domain.Entity.Elements.ObjetosMagicos;

import Domain.Entity.Characters.Players.Hero;
import javafx.scene.image.Image;

public class EscudoEnergia extends AbstractItem {

    public EscudoEnergia() {
        super("assets/textures/escudoEnergia.png");
    }

    public void use(Hero target) {
        target.addShield(4); // Asumiendo que Hero tiene un setShield
        System.out.println(target.getName() + " recibe un escudo que absorbe 4 de daño.");
    }

    @Override
    public String getName() {
        return "escudoEnergia";
    }

    @Override
    public String getType() {
        return "escudoEnergia";
    }

    public Image getImage() {
        return super.getImage(); // usa la implementación de la clase base AbstractItem
    }
}