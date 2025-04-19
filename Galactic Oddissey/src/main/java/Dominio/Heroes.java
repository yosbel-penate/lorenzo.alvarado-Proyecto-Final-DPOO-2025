package Dominio;
public abstract class Heroes extends Personaje {
    protected int rangoMovimiento;
    protected int rangoAtaque;

    public Heroes(String nombre, int vida, int dano, String habilidadEspecial,
                  int rangoMovimiento, int rangoAtaque) {
        super(nombre, vida, dano, habilidadEspecial);
        this.rangoMovimiento = rangoMovimiento;
        this.rangoAtaque = rangoAtaque;
    }

    public abstract void mover(Casillas casillaDestino);
    public abstract void atacar(Enemigo objetivo);
    public abstract void usarHabilidad();
}

