package View.Maps;

import Domain.Entity.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

public class GameUtils {
    public static boolean isTileBlocked(int tileX, int tileY) {
        for (Entity e : FXGL.getGameWorld().getEntitiesByType(EntityType.OBSTACULOS)) {
            int ex = (int) (e.getX() / 56);  // 56 es TILE_SIZE
            int ey = (int) (e.getY() / 56);
            if (ex == tileX && ey == tileY) {
                return true;
            }
        }
        return false;
    }
}
