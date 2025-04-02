import java.util.ArrayList;
import java.util.List;

public class Tablero {
    private List<Casillas> casillas;
    private int ancho;
    private int alto;

    public Tablero(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.casillas = new ArrayList<>();
    }

    public void generarNivel(int nivel) {
        // Implementación

    }
    public Casillas obtenerCasillas(int x, int y) {
        // Implementación
        return null;
    }
}
