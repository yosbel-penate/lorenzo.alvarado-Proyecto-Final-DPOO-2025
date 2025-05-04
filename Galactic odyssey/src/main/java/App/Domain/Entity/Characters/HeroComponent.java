package Domain.Entity.Characters;

import com.almasb.fxgl.entity.component.Component;

public class HeroComponent extends Component {
    private int speed = 5;

    // Método para mover al héroe hacia la derecha
    public void moveRight() {
        entity.translateX(speed);
    }

    // Método para mover al héroe hacia la izquierda
    public void moveLeft() {
        entity.translateX(-speed);
    }

    // Método para mover al héroe hacia arriba
    public void moveUp() {
        entity.translateY(-speed);
    }

    // Método para mover al héroe hacia abajo
    public void moveDown() {
        entity.translateY(speed);
    }
}
