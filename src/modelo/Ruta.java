package modelo;
import java.util.ArrayList;

public class Ruta {
    private int id;
    private String nombre;
    private Lugar origen;
    private Lugar destino;
    private ArrayList<Tramo> tramos = new ArrayList<Tramo>();

    public Ruta(int id, String nombre, Lugar origen, Lugar destino) {
        this.id = id;
        this.nombre = nombre;
        this.origen = origen;
        this.destino = destino;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Lugar getOrigen() { return origen; }
    public Lugar getDestino() { return destino; }
    public ArrayList<Tramo> getTramos() { return new ArrayList<Tramo>(tramos); }
    public void agregarTramo(Tramo tramo, int orden) {
        tramos.add(orden - 1, tramo);
        renumerar();
    }
    public void moverTramo(Tramo tramo, int orden) {
        tramos.remove(tramo);
        tramos.add(orden - 1, tramo);
        renumerar();
    }
    public void eliminarTramo(Tramo tramo) {
        tramos.remove(tramo);
        renumerar();
    }
    private void renumerar() {
        for (int i = 0; i < tramos.size(); i++) {
            tramos.get(i).setOrden(i + 1);
        }
    }
}
