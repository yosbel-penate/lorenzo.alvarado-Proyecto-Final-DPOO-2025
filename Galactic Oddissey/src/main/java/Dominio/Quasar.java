package Dominio;
public class Quasar extends Heroes{
    public Quasar(String nombre, int vida, int dano, String habilidadEspecial,
                        int rangoMovimiento, int rangoAtaque) {
        super(nombre, vida, dano, habilidadEspecial, rangoMovimiento, rangoAtaque);
    }

    @Override
    public void mover(Casillas casillaDestino) {
        // Implementación específica
    }

    @Override
    public void atacar(Enemigo objetivo) {
        // Implementación específica
    }
    @Override
    public void usarHabilidad() {
        // Implementación específica
    }
    public void reparacionRapida() {}
}
