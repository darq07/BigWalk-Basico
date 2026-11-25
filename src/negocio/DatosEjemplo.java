package negocio;
import java.time.LocalDate;

public class DatosEjemplo {
    public static void cargar(SistemaBigWalk sistema) {
        GestorRutas rutas = sistema.getGestorRutas();
        GestorReportes reportes = sistema.getGestorReportes();
        EvaluadorRiesgo evaluador = sistema.getEvaluador();
        rutas.registrarRuta("Ruta A - avenida", "UDLA Granados", "ACCESO_CAMPUS", "Parada principal", "PARADA");
        rutas.agregarTramo(1, "Salida del campus", 1, 120);
        rutas.agregarTramo(1, "Avenida principal", 2, 300);
        rutas.agregarTramo(1, "Cruce peatonal", 3, 80);
        evaluador.calificarTramo(1, 1, 1, 0, 1, 0);
        evaluador.calificarTramo(1, 2, 2, 1, 3, 1);
        evaluador.calificarTramo(1, 3, 0, 1, 4, 0);
        reportes.registrarReporte(1, 2, "ILUMINACION", "Luminaria dañada", LocalDate.now(), 4, false);
        reportes.actualizarEstado(1, "CONFIRMADO");
        reportes.registrarReporte(1, 2, "OBSTACULO", "Vehículo sobre la acera", LocalDate.now(), 3, false);
        rutas.registrarRuta("Ruta B - sin calificar", "UDLA Granados", "ACCESO_CAMPUS", "Parada principal", "PARADA");
        rutas.agregarTramo(2, "Calle lateral", 1, 400);
        rutas.registrarRuta("Ruta C - vacía", "UDLA Granados", "ACCESO_CAMPUS", "Estacionamiento norte", "ESTACIONAMIENTO");
    }
}
