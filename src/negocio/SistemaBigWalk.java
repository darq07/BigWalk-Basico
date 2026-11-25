package negocio;

public class SistemaBigWalk {
    private GestorRutas gestorRutas;
    private GestorReportes gestorReportes;
    private EvaluadorRiesgo evaluador;
    public SistemaBigWalk() {
        gestorRutas = new GestorRutas();
        gestorReportes = new GestorReportes(gestorRutas);
        evaluador = new EvaluadorRiesgo(gestorRutas);
    }
    public GestorRutas getGestorRutas() { return gestorRutas; }
    public GestorReportes getGestorReportes() { return gestorReportes; }
    public EvaluadorRiesgo getEvaluador() { return evaluador; }
}
