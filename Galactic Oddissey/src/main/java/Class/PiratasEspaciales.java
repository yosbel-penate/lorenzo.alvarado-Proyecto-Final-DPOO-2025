class PirataEspacial extends Enemigo {
    public PirataEspacial(String nombre, int vida, int dano, String habilidadEspecial,
                        String comportamientoIA, String frecuenciaHabilidad) {
        super(nombre, vida, dano, habilidadEspecial, comportamientoIA, frecuenciaHabilidad);
    }

    @Override
    public void actuar() {
        // Implementación específica
    }
    public void robar() {
        // Implementación específica de PirataEspacial
    }
}