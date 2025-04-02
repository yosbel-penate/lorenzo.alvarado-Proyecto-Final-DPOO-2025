class BombaPlasma extends ObjetoMagico {
    private int dano;

    public BombaPlasma(String nombre, String efecto, int rangoUso, int dano) {
        super(nombre, efecto, rangoUso);
        this.dano = dano;
    }

    @Override
    public void usar() {}

    public void explota() {}
}




