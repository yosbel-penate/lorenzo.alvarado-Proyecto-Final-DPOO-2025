package Dominio;
import java.util.ArrayList;
    import java.util.List;

    public class Juego {
        private List<Enemigo> enemigos;
        private Tablero tablero;
        private int turnoActual;

        public Juego() {
            this.enemigos = new ArrayList<>();
            this.tablero = new Tablero(10, 10); // Ejemplo de tamaño
            this.turnoActual = 0;
        }

        public void iniciarPartida() {
            // Implementación
        }

        public void finalizarTurno() {
            // Implementación
        }

        public boolean verificarVictoria() {
            // Implementación
            return false;
        }
    }
