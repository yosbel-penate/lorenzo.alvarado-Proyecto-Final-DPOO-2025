package Domain.Entity.Elements;

import Domain.Entity.Elements.ObjetoRecurso;

class PocionCurativa extends ObjetoRecurso {
    public PocionCurativa() { nombre = "Poción Curativa"; }
    public void usar() {
        System.out.println("Restaura 3-5 puntos de salud.");
    }
}