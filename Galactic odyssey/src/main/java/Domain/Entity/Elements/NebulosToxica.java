package Domain.Entity.Elements;

import Domain.Entity.Elements.PeligroAmbiental;

class NebulosaToxica extends PeligroAmbiental {
    public NebulosaToxica() { nombre = "Nebulosa Tóxica"; }
    public void activar() {
        System.out.println("Daña a los personajes sin protección: 1-2 puntos por turno.");
    }
}