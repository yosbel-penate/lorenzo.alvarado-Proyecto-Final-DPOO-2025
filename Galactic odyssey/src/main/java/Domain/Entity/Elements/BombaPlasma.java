package Domain.Entity.Elements;

import Domain.Entity.Elements.ObjetoRecurso;

class BombaPlasma extends ObjetoRecurso {
    public BombaPlasma() { nombre = "Bomba de Plasma"; }
    public void usar() {
        System.out.println("Explota en un radio de 3 casillas, causando 4-6 puntos de daño.");
    }
}
