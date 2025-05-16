package Domain.Entity.Elements;

import Domain.Entity.Elements.PeligroAmbiental;

class TrampaAntigua extends PeligroAmbiental {
    public TrampaAntigua() { nombre = "Trampa Antigua"; }
    public void activar() {
        System.out.println("Se activa al pasar por encima y causa 3-5 puntos de daño.");
    }
}