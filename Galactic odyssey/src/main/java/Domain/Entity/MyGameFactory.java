package Domain.Entity;

import Domain.Entity.Characters.Hero;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.EntityFactory;
import java.util.List;

public class MyGameFactory implements EntityFactory {

    private Hero hero; // Reference to the hero
    private List<Hero> heroesInGame;

    public MyGameFactory(Hero hero, List<Hero> heroesInGame) {
        this.hero = hero;
        this.heroesInGame = heroesInGame;
    }

    @Spawns("obstaculo")
    public Entity spawnObstaculo(SpawnData data) {
        return new EntityBuilder()
                .from(data)
                .type(EntityType.OBSTACLE) // Make sure EntityType is defined correctly
                .build();
    }
}
