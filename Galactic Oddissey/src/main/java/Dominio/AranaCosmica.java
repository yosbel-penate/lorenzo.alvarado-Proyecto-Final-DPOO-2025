package Dominio;
class AranaCosmica extends Enemigo {
    public AranaCosmica(String nombre, int vida, int dano, String habilidadEspecial,
                        String comportamientoIA, String frecuenciaHabilidad) {
        super(nombre, vida, dano, habilidadEspecial, comportamientoIA, frecuenciaHabilidad);
    }

    @Override
    public void actuar() {
        // Implementación específica
    }
    public void lanzarRed() {
        // Implementación específica de ArenaCosmica
    }
}

