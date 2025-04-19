package Dominio;
public class GiganteDeLava extends Enemigo{
    public GiganteDeLava(String nombre, int vida, int dano, String habilidadEspecial,
                        String comportamientoIA, String frecuenciaHabilidad) {
        super(nombre, vida, dano, habilidadEspecial, comportamientoIA, frecuenciaHabilidad);
    }

    @Override
    public void actuar() {
        // Implementación específica
    }
    public void errupcion(){

    }
}
