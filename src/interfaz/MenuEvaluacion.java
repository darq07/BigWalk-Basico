package interfaz;
import modelo.*;
import negocio.EvaluadorRiesgo;

public class MenuEvaluacion {
    private Consola consola;
    private EvaluadorRiesgo evaluador;
    private MenuRutas menuRutas;
    public MenuEvaluacion(Consola consola, EvaluadorRiesgo evaluador, MenuRutas menuRutas) {
        this.consola = consola; this.evaluador = evaluador; this.menuRutas = menuRutas;
    }
    public void ejecutar() {
        while (!consola.estaCerrada()) {
            System.out.println("\nMÓDULO 3 - EVALUACIÓN DEL RIESGO\n1. Calificar tramo\n2. Calcular índice de tramo\n3. Evaluar ruta completa\n0. Volver");
            Integer opcion = consola.entero("Opción");
            if (opcion == null) { continue; }
            if (opcion == 0) { return; }
            if (opcion == 1) { calificar(); }
            else if (opcion == 2) { indiceTramo(); }
            else if (opcion == 3) { evaluarRuta(); }
            else { consola.error("Opción inválida."); }
        }
    }
    private void calificar() {
        Ruta ruta = menuRutas.elegirRuta(); if (ruta == null) { return; }
        Tramo tramo = menuRutas.elegirTramo(ruta); if (tramo == null) { return; }
        boolean actualizacion = tramo.estaCalificado();
        Integer iluminacion = consola.entero("Iluminación deficiente (0-5)"); if (iluminacion == null) { return; }
        Integer acera = consola.entero("Problemas de acera (0-5)"); if (acera == null) { return; }
        Integer cruces = consola.entero("Cruces difíciles (0-5)"); if (cruces == null) { return; }
        Integer obstaculos = consola.entero("Obstáculos (0-5)"); if (obstaculos == null) { return; }
        if (!evaluador.calificarTramo(ruta.getId(), tramo.getId(), iluminacion, acera, cruces, obstaculos)) { consola.error(evaluador.getError()); return; }
        if (actualizacion) { consola.exito("Calificación actualizada."); } else { consola.exito("Calificación registrada."); }
        System.out.println("Suma: " + tramo.getCalificacion().getSuma() + "/20");
        for (String aviso : evaluador.advertencias(tramo)) { System.out.println("[AVISO] " + aviso); }
    }
    private void indiceTramo() {
        Ruta ruta = menuRutas.elegirRuta(); if (ruta == null) { return; }
        Tramo tramo = menuRutas.elegirTramo(ruta); if (tramo == null) { return; }
        double indice = evaluador.calcularIndiceTramo(ruta.getId(), tramo.getId());
        if (indice < 0) { consola.error(evaluador.getError()); return; }
        System.out.println("Tramo: " + tramo.getNombre() + "\nÍndice: " + consola.numero(indice) + "/100\nReportes confirmados: " + tramo.contarConfirmados());
    }
    private void evaluarRuta() {
        Ruta ruta = menuRutas.elegirRuta(); if (ruta == null) { return; }
        EvaluacionRuta resultado = evaluador.evaluarRuta(ruta.getId());
        if (resultado == null) { consola.error(evaluador.getError()); return; }
        System.out.println("Ruta: " + resultado.getRuta().getNombre());
        System.out.println("Tramos evaluados: " + resultado.getCantidadTramos());
        System.out.println("Distancia total: " + consola.numero(resultado.getDistancia()) + " m");
        System.out.println("Índice global: " + consola.numero(resultado.getIndice()) + " / 100");
        System.out.println("Nivel de exposición observada: " + resultado.getNivel());
        System.out.println("Reportes confirmados: " + resultado.getConfirmados());
        System.out.println("Resultado orientativo; no garantiza la seguridad del recorrido.");
    }
}
