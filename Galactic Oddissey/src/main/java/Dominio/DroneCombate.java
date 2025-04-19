package Dominio;
public class DroneCombate extends Enemigo {
    public DroneCombate(String nombre, int vida, int dano, String habilidadEspecial,
                        String comportamientoIA, String frecuenciaHabilidad) {
        super(nombre, vida, dano, habilidadEspecial, comportamientoIA, frecuenciaHabilidad);
    }

    @Override
    public void actuar() {
        // Implementación específica
    }

    public void escanear() {
        // Habilidad especial
    }
}


