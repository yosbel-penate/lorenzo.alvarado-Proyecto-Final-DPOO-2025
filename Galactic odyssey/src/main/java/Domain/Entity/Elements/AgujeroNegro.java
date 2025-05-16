package Domain.Entity.Elements;

import Domain.Entity.Elements.PeligroAmbiental;

class AgujeroNegro extends PeligroAmbiental {
    public AgujeroNegro() { nombre = "Agujero Negro"; }
    public void activar() {
        System.out.println("Atrae a los personajes hacia sí y causa 4-6 puntos de daño.");
    }
}