package Domain.Entity.Elements;

import Domain.Entity.Elements.ObjetoRecurso;

class EscudoEnergia extends ObjetoRecurso {
    public EscudoEnergia() { nombre = "Escudo de Energía"; }
    public void usar() {
        System.out.println("Absorbe 4 puntos de daño.");
    }
}