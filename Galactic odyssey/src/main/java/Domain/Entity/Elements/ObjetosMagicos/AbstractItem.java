package Domain.Entity.Elements.ObjetosMagicos;

// Item.java
import javafx.scene.image.Image;

public abstract class AbstractItem implements Item {
    protected String imagePath;
    protected String type; // ej: "pocion", "escudo", "bomba"

    public AbstractItem(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getType() {
        return type;
    }
    public void setType(){

    }
    public Image getImage() {
        return new Image(imagePath);
    }
}

