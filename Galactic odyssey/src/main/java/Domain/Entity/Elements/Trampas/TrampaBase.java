package Domain.Entity.Elements.Trampas;

import Domain.Entity.Characters.Players.Hero;
import com.almasb.fxgl.entity.Entity;

import java.util.List;

public interface TrampaBase {
    void setEntity(Entity entity);

    void reveal();
    void applyEffect(List<Hero> heroes);
}
