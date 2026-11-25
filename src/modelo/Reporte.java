package modelo;
import java.time.LocalDate;

public class Reporte {
    private int id;
    private Tramo tramo;
    private String tipo;
    private String descripcion;
    private LocalDate fecha;
    private int gravedad;
    private String estado;
    private LocalDate fechaActualizacion;

    public Reporte(int id, Tramo tramo, String tipo, String descripcion, LocalDate fecha, int gravedad) {
        this.id = id;
        this.tramo = tramo;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.gravedad = gravedad;
        estado = "PENDIENTE";
        fechaActualizacion = LocalDate.now();
    }
    public int getId() { return id; }
    public Tramo getTramo() { return tramo; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFecha() { return fecha; }
    public int getGravedad() { return gravedad; }
    public String getEstado() { return estado; }
    public LocalDate getFechaActualizacion() { return fechaActualizacion; }
    public void setEstado(String estado) {
        this.estado = estado;
        fechaActualizacion = LocalDate.now();
    }
    public boolean estaActivo() { return !estado.equals("CERRADO"); }
    public String toString() {
        return "ID " + id + " | Tramo " + tramo.getId() + " | " + tipo + " | " + fecha
                + " | Gravedad " + gravedad + " | " + estado + " | " + descripcion;
    }
}
