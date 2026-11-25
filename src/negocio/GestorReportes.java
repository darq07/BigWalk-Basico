package negocio;
import modelo.*;
import java.util.ArrayList;
import java.time.LocalDate;

public class GestorReportes extends GestorBase {
    private GestorRutas gestorRutas;
    private ArrayList<Reporte> reportes = new ArrayList<Reporte>();
    private int siguienteId = 1;
    public GestorReportes(GestorRutas gestorRutas) { this.gestorRutas = gestorRutas; }
    public ArrayList<Reporte> getReportes() { return new ArrayList<Reporte>(reportes); }
    private boolean validarDatos(String tipo, LocalDate fecha) {
        if (!Validador.tipoReporte(tipo)) { setError("Tipo de reporte inválido."); return false; }
        if (fecha == null || fecha.isAfter(LocalDate.now()) || fecha.isBefore(LocalDate.now().minusDays(365))) {
            setError("Fecha inválida: no puede ser futura ni anterior a 365 días."); return false;
        }
        return true;
    }

    public Reporte buscarDuplicado(int idRuta, int idTramo, String tipo, LocalDate fecha) {
        setError("");
        Tramo tramo = gestorRutas.buscarTramo(idRuta, idTramo);
        if (tramo == null) { setError(gestorRutas.getError()); return null; }
        if (!validarDatos(tipo, fecha)) { return null; }
        for (Reporte reporte : tramo.getReportes()) {
            if (reporte.getTipo().equals(tipo) && reporte.estaActivo()
                    && !reporte.getFecha().isBefore(fecha.minusDays(7))
                    && !reporte.getFecha().isAfter(fecha.plusDays(7))) { return reporte; }
        }
        return null;
    }
    public Reporte registrarReporte(int idRuta, int idTramo, String tipo, String descripcion, LocalDate fecha, int gravedad, boolean confirmarDuplicado) {
        setError("");
        if (!Validador.texto(descripcion, 5, 200)) { setError("Descripción: 5 a 200 caracteres."); return null; }
        if (!Validador.rango(gravedad, 1, 5)) { setError("Gravedad: 1 a 5."); return null; }
        Reporte duplicado = buscarDuplicado(idRuta, idTramo, tipo, fecha);
        if (!getError().isEmpty()) { return null; }
        if (duplicado != null && !confirmarDuplicado) { setError("Posible duplicado: reporte " + duplicado.getId() + ". Confirme para registrar."); return null; }
        Tramo tramo = gestorRutas.buscarTramo(idRuta, idTramo);
        Reporte reporte = new Reporte(siguienteId, tramo, tipo, descripcion.trim(), fecha, gravedad);
        siguienteId++;
        reportes.add(reporte);
        tramo.agregarReporte(reporte);
        return reporte;
    }
    public Reporte buscarReporte(int id) {
        setError("");
        for (Reporte reporte : reportes) { if (reporte.getId() == id) { return reporte; } }
        setError("No existe el reporte " + id + "."); return null;
    }
    public boolean actualizarEstado(int id, String nuevo) {
        Reporte reporte = buscarReporte(id);
        if (reporte == null) { return false; }
        if (!"PENDIENTE".equals(nuevo) && !"CONFIRMADO".equals(nuevo) && !"CERRADO".equals(nuevo)) { setError("Estado inválido."); return false; }
        String actual = reporte.getEstado();
        if (actual.equals("CERRADO") || actual.equals(nuevo) || nuevo.equals("PENDIENTE")) {
            setError("Cambio de estado no permitido: " + actual + " a " + nuevo + "."); return false;
        }
        reporte.setEstado(nuevo);
        return true;
    }
    public ArrayList<Reporte> listarPorTramo(int idTramo) {
        setError("");
        Tramo tramo = gestorRutas.buscarTramoGeneral(idTramo);
        if (tramo == null) { setError(gestorRutas.getError()); return null; }
        return tramo.getReportes();
    }
}
