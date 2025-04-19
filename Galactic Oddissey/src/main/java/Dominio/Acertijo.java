package Dominio;// Clase Acertijo

    public class Acertijo {
        private String descripcion;
        private String solucion;

        public Acertijo(String descripcion, String solucion) {
            this.descripcion = descripcion;
            this.solucion = solucion;
        }

        public boolean resolver(String intento) {
            return solucion.equals(intento);
        }
    }




