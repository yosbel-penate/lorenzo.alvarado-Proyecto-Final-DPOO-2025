package Dominio;
// Clase base Personaje
public abstract class Personaje {
    protected String nombre;
    protected int vida;
    protected int dano;
    protected String habilidadEspecial;

    public Personaje(String nombre, int vida, int dano, String habilidadEspecial) {
        this.nombre = nombre;
        this.vida = vida;
        this.dano = dano;
        this.habilidadEspecial = habilidadEspecial;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getDano() { return dano; }
    public String getHabilidadEspecial() { return habilidadEspecial; }

    public void setVida(int vida) { this.vida = vida; }
}


