package Domain.Entity.Elements.ObjetosMagicos;

import Domain.Entity.Characters.Players.Hero;
import javafx.scene.image.Image;

public interface Item {
    void use(Hero target); // Aplica el efecto al héroe

    String getName();

    Image getImage();

    String getType();
}

