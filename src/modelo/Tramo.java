package modelo;
import java.util.ArrayList;

public class Tramo {
    private int id;
    private String nombre;
    private int orden;
    private double distancia;
    private CalificacionCondiciones calificacion;
    private ArrayList<Reporte> reportes = new ArrayList<Reporte>();

    public Tramo(int id, String nombre, int orden, double distancia) {
        this.id = id;
        this.nombre = nombre;
        this.orden = orden;
        this.distancia = distancia;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getOrden() { return orden; }
    public double getDistancia() { return distancia; }
    public CalificacionCondiciones getCalificacion() { return calificacion; }
    public ArrayList<Reporte> getReportes() { return new ArrayList<Reporte>(reportes); }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setOrden(int orden) { this.orden = orden; }
    public void setDistancia(double distancia) { this.distancia = distancia; }
    public void setCalificacion(CalificacionCondiciones calificacion) { this.calificacion = calificacion; }
    public void agregarReporte(Reporte reporte) { reportes.add(reporte); }
    public boolean estaCalificado() { return calificacion != null; }
    public int contarConfirmados() {
        int total = 0;
        for (Reporte reporte : reportes) {
            if (reporte.getEstado().equals("CONFIRMADO")) { total++; }
        }
        return total;
    }
}
