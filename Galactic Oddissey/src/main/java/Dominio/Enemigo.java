package Dominio;
// Clase Enemigo
 public abstract class Enemigo extends Personaje {
    protected String comportamientoIA;
    protected String frecuenciaHabilidad;

    public Enemigo(String nombre, int vida, int dano, String habilidadEspecial,
                   String comportamientoIA, String frecuenciaHabilidad) {
        super(nombre, vida, dano, habilidadEspecial);
        this.comportamientoIA = comportamientoIA;
        this.frecuenciaHabilidad = frecuenciaHabilidad;
    }

    public abstract void actuar();
}

