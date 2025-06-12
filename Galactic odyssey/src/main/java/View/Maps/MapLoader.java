package View.Maps;

import Domain.Entity.Elements.ObjetosMagicos.EscudoEnergia;
import Domain.Entity.Elements.ObjetosMagicos.Item;
import Domain.Entity.Elements.ObjetosMagicos.ItemEntity;
import Domain.Entity.Elements.ObjetosMagicos.PocionCurativa;
import Domain.Entity.Elements.Trampas.AgujeroNegro;
import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;

public class MapLoader {
    public void loadMap(String nameMap) {
        FXGL.setLevelFromMap(nameMap);

        FXGL.getGameWorld().getEntities().forEach(e -> {
            if (!e.getProperties().exists("type"))
                return;

            String type = e.getString("type").toLowerCase();

            switch (type) {
                case "obstaculo":
                    e.setType(EntityType.OBSTACULOS);
                    break;

                case "pocion":
                    spawnItemFromMap(e, new PocionCurativa());
                    break;

                case "escudo":
                    spawnItemFromMap(e, new EscudoEnergia());
                    break;

                // Agrega más tipos si tenés más ítems
            }
        });
    }
    public void spawnTrampas() {
        AgujeroNegro agujero = new AgujeroNegro();
        agujero.setPosition(6, 8);

        SpawnData data = new SpawnData(6, 8)
                .put("elemento", agujero)
                .put("spriteName", "blackhole.png");

        FXGL.spawn("trampa", data);
    }

    private void spawnItemFromMap(Entity tileEntity, Item item) {
        FXGL.spawn("item", tileEntity.getX(), tileEntity.getY())
                .getComponent(ItemEntity.class)
                .setItem(item);

        // Eliminamos la entidad del mapa base (vacía)
        FXGL.getGameWorld().removeEntity(tileEntity);
    }
}
