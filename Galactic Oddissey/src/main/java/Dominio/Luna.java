package Dominio;
public class Luna extends Heroes{
    public Luna(String nombre, int vida, int dano, String habilidadEspecial,
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
    public void instintoDeCazadora() {}
}
