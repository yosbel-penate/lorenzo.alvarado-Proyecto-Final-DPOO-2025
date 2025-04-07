public abstract class ObjetoMagico {
    protected String nombre;
    protected String efecto;
    protected int rangoUso;

    public ObjetoMagico(String nombre, String efecto, int rangoUso) {
        this.nombre = nombre;
        this.efecto = efecto;
        this.rangoUso = rangoUso;
    }

    public abstract void usar();
}


