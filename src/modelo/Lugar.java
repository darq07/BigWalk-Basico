package modelo;

public class Lugar {
    private String nombre;
    private String tipo;

    public Lugar(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String toString() { return nombre + " (" + tipo + ")"; }
}
