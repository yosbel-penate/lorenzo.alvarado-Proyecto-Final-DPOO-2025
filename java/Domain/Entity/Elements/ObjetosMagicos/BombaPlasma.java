package Domain.Entity.Elements.ObjetosMagicos;

import Domain.Entity.Characters.Enemies.EnemyEntity;
import Domain.Entity.Characters.Players.Hero;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.List;

public class BombaPlasma extends AbstractItem {
    public BombaPlasma(){
        super("assets/textures/BombaPlasma.png");
    }

    public void use(Point2D targetPosition, List<EnemyEntity> enemies) {
        System.out.println("Bomba de Plasma lanzada hacia " + targetPosition +
                ": explota en radio de 3 casillas, causando 4-6 de daño.");

        for (EnemyEntity enemy : enemies) {
            Point2D enemyPos = new Point2D(enemy.getLogic().getX(), enemy.getLogic().getY()); // ✅
            double distance = targetPosition.distance(enemyPos);

            if (distance <= 3) {
                int damage = 4 + (int) (Math.random() * 3); // Daño entre 4 y 6
                enemy.getLogic().takeDamage(damage);
                enemy.showDamage(damage); // si tienes esta función
                System.out.println("Enemigo en " + enemyPos + " recibe " + damage + " de daño.");
            }
        }
    }

    @Override
    public void use(Hero target) {
        System.out.println("Este objeto debe lanzarse a una posición, no a un héroe directamente.");
    }

    @Override
    public String getName() {
        return "BombaPlasma";
    }
    public String getType() {
        return "BombaPlasma"; // <- AQUÍ ESTABA FALTANDO
    }
    public Image getImage() {
        return super.getImage(); // usa la implementación de la clase base AbstractItem
    }
    }

