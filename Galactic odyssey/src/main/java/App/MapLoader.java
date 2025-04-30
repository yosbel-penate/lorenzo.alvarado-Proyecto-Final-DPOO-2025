package App;

import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.property.MapProperty;

public class MapLoader {

    public void loadMap() {
        FXGL.setLevelFromMap("MapGame1.tmx");
    }
}
