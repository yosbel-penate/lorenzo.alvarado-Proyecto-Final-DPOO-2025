package Domain.Entity.Elements.ObjetosMagicos;

import Domain.Entity.Characters.Players.Hero;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.Image;

public class PocionCurativa extends AbstractItem {

    private final String name = "pocion";

    public PocionCurativa() {
        super("assets/textures/pocion.png");
    }

    @Override
    public void use(Hero target) {
        int heal = FXGL.random(3, 5);
        target.restoreHealth(heal);
        System.out.println(target.getName() + " recupera " + heal + " puntos de salud.");
    }

    @Override
    public String getName() {
        return name;
    }

    public Image getImage() {
        return super.getImage(); // usa la implementación de la clase base AbstractItem
    }

    public String getType() {
        return  "pocion"; // <- AQUÍ ESTABA FALTANDO
    }
    public String getImagePath() {
        return imagePath;
    }
}
