package interfaz;
import negocio.SistemaBigWalk;

public class MenuPrincipal {
    private Consola consola;
    private MenuRutas rutas;
    private MenuReportes reportes;
    private MenuEvaluacion evaluacion;
    public MenuPrincipal(Consola consola, SistemaBigWalk sistema) {
        this.consola = consola;
        rutas = new MenuRutas(consola, sistema.getGestorRutas());
        reportes = new MenuReportes(consola, sistema.getGestorReportes(), rutas);
        evaluacion = new MenuEvaluacion(consola, sistema.getEvaluador(), rutas);
    }
    public void ejecutar() {
        while (!consola.estaCerrada()) {
            System.out.println("\n========== BIGWALK ==========\n1. Rutas y tramos\n2. Reportes de condiciones\n3. Evaluación del riesgo\n0. Salir");
            Integer opcion = consola.entero("Opción");
            if (opcion == null) { continue; }
            if (opcion == 0) { return; }
            if (opcion == 1) { rutas.ejecutar(); }
            else if (opcion == 2) { reportes.ejecutar(); }
            else if (opcion == 3) { evaluacion.ejecutar(); }
            else { consola.error("Opción inválida."); }
        }
    }
}
