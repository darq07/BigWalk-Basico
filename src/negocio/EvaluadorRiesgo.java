package negocio;
import modelo.*;
import java.util.ArrayList;

public class EvaluadorRiesgo extends GestorBase {
    private GestorRutas gestorRutas;
    public EvaluadorRiesgo(GestorRutas gestorRutas) { this.gestorRutas = gestorRutas; }

    // RF-07: primero se validan todos los datos, luego se guarda la calificación.
    public boolean calificarTramo(int idRuta, int idTramo, int iluminacion, int acera, int cruces, int obstaculos) {
        setError("");
        Tramo tramo = gestorRutas.buscarTramo(idRuta, idTramo);
        if (tramo == null) { setError(gestorRutas.getError()); return false; }
        if (!Validador.rango(iluminacion, 0, 5) || !Validador.rango(acera, 0, 5)
                || !Validador.rango(cruces, 0, 5) || !Validador.rango(obstaculos, 0, 5)) {
            setError("Las cuatro calificaciones deben estar entre 0 y 5."); return false;
        }
        tramo.setCalificacion(new CalificacionCondiciones(iluminacion, acera, cruces, obstaculos));
        return true;
    }
    public ArrayList<String> advertencias(Tramo tramo) {
        ArrayList<String> avisos = new ArrayList<String>();
        if (tramo == null || !tramo.estaCalificado()) { return avisos; }
        for (Reporte reporte : tramo.getReportes()) {
            if (reporte.getEstado().equals("CONFIRMADO") && reporte.getGravedad() >= 4
                    && tramo.getCalificacion().getPuntaje(reporte.getTipo()) == 0) {
                avisos.add("Reporte confirmado " + reporte.getId() + " de " + reporte.getTipo() + " con gravedad alta, pero calificación 0.");
            }
        }
        return avisos;
    }
    // RF-08: -1 indica que no se puede calcular.
    public double calcularIndiceTramo(int idRuta, int idTramo) {
        setError("");
        Tramo tramo = gestorRutas.buscarTramo(idRuta, idTramo);
        if (tramo == null) { setError(gestorRutas.getError()); return -1; }
        if (!tramo.estaCalificado()) { setError("Tramo sin calificar. Complete RF-07."); return -1; }
        if (!calificacionValida(tramo.getCalificacion())) { setError("Calificación inválida. Complete RF-07."); return -1; }
        return Validador.redondear(tramo.getCalificacion().calcularIndice());
    }
    private boolean calificacionValida(CalificacionCondiciones c) {
        return c != null && Validador.rango(c.getIluminacion(),0,5) && Validador.rango(c.getAcera(),0,5)
                && Validador.rango(c.getCruces(),0,5) && Validador.rango(c.getObstaculos(),0,5);
    }
    public String calcularNivel(double indice) {
        if (!(indice >= 0 && indice <= 100)) { return "INVALIDO"; }
        if (indice <= 33.33) { return "BAJA"; }
        if (indice <= 66.66) { return "MEDIA"; }
        return "ALTA";
    }
    // RF-09: el índice global solo se calcula cuando todos los tramos son válidos.
    public EvaluacionRuta evaluarRuta(int idRuta) {
        setError("");
        double distancia = gestorRutas.calcularDistanciaTotal(idRuta);
        if (distancia < 0) { setError(gestorRutas.getError()); return null; }
        Ruta ruta = gestorRutas.buscarRuta(idRuta);
        String pendientes = "";
        for (Tramo tramo : ruta.getTramos()) {
            if (!calificacionValida(tramo.getCalificacion())) {
                pendientes += "ID " + tramo.getId() + " (" + tramo.getNombre() + ") ";
            }
        }
        if (!pendientes.isEmpty()) { setError("Tramos sin calificación válida: " + pendientes + ". Complete RF-07."); return null; }
        double suma = 0;
        int confirmados = 0;
        for (Tramo tramo : ruta.getTramos()) {
            suma += tramo.getCalificacion().calcularIndice() * tramo.getDistancia();
            confirmados += tramo.contarConfirmados();
        }
        double indice = Validador.redondear(suma / distancia);
        return new EvaluacionRuta(ruta, Validador.redondear(distancia), indice, calcularNivel(indice), ruta.getTramos().size(), confirmados);
    }
}
