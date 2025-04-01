// Clase Casilla



public class Casillas {
    private int x;
    private int y;
    private String tipo;
    private String contenido;
    private String peligro;

    public Casillas(int x, int y, String tipo, String contenido, String peligro) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.contenido = contenido;
        this.peligro = peligro;
    }

    public boolean estaOcupada() {
        // Implementación
        return false;
    }
}


