package interfaz;
import modelo.*;
import negocio.GestorRutas;

public class MenuRutas {
    private Consola consola;
    private GestorRutas gestor;
    public MenuRutas(Consola consola, GestorRutas gestor) { this.consola = consola; this.gestor = gestor; }
    public void ejecutar() {
        while (!consola.estaCerrada()) {
            System.out.println("\nMÓDULO 1 - RUTAS Y TRAMOS\n1. Registrar ruta\n2. Listar rutas\n3. Gestionar tramos\n4. Calcular distancia total\n0. Volver");
            Integer opcion = consola.entero("Opción");
            if (opcion == null) { continue; }
            if (opcion == 0) { return; }
            if (opcion == 1) { registrar(); }
            else if (opcion == 2) { listarRutas(); }
            else if (opcion == 3) { Ruta ruta = elegirRuta(); if (ruta != null) { gestionar(ruta); } }
            else if (opcion == 4) { distancia(); }
            else { consola.error("Opción inválida."); }
        }
    }
    private void registrar() {
        String nombre = consola.texto("Nombre de ruta");
        String origen = consola.texto("Origen");
        String tipoOrigen = consola.tipoLugar();
        if (tipoOrigen == null) { return; }
        String destino = consola.texto("Destino");
        String tipoDestino = consola.tipoLugar();
        if (tipoDestino == null) { return; }
        Ruta ruta = gestor.registrarRuta(nombre, origen, tipoOrigen, destino, tipoDestino);
        if (ruta == null) { consola.error(gestor.getError()); return; }
        consola.exito("Ruta " + ruta.getId() + " - " + ruta.getNombre() + ": " + ruta.getOrigen() + " -> " + ruta.getDestino() + ". Agregue tramos.");
    }
    public void listarRutas() {
        if (gestor.getRutas().isEmpty()) { System.out.println("No hay rutas registradas."); }
        for (Ruta ruta : gestor.getRutas()) {
            System.out.println("ID " + ruta.getId() + " | " + ruta.getNombre() + " | " + ruta.getOrigen() + " -> " + ruta.getDestino() + " | Tramos: " + ruta.getTramos().size());
        }
    }
    public Ruta elegirRuta() {
        listarRutas();
        if (gestor.getRutas().isEmpty()) { return null; }
        Integer id = consola.entero("ID de ruta");
        if (id == null) { return null; }
        Ruta ruta = gestor.buscarRuta(id);
        if (ruta == null) { consola.error(gestor.getError()); }
        return ruta;
    }
    public void listarTramos(Ruta ruta) {
        if (ruta.getTramos().isEmpty()) { System.out.println("La ruta no tiene tramos."); }
        for (Tramo tramo : ruta.getTramos()) {
            String nota = "Sin calificar";
            if (tramo.estaCalificado()) {
                CalificacionCondiciones c = tramo.getCalificacion();
                nota = "L=" + c.getIluminacion() + ", A=" + c.getAcera() + ", C=" + c.getCruces() + ", O=" + c.getObstaculos();
            }
            System.out.println("ID " + tramo.getId() + " | Orden " + tramo.getOrden() + " | " + tramo.getNombre() + " | " + consola.numero(tramo.getDistancia()) + " m | " + nota + " | Reportes: " + tramo.getReportes().size());
        }
    }
    public Tramo elegirTramo(Ruta ruta) {
        listarTramos(ruta);
        if (ruta.getTramos().isEmpty()) { return null; }
        Integer id = consola.entero("ID de tramo");
        if (id == null) { return null; }
        Tramo tramo = gestor.buscarTramo(ruta.getId(), id);
        if (tramo == null) { consola.error(gestor.getError()); }
        return tramo;
    }
    private void gestionar(Ruta ruta) {
        while (!consola.estaCerrada()) {
            System.out.println("\nTRAMOS - " + ruta.getNombre() + "\n1. Agregar\n2. Modificar\n3. Consultar\n4. Eliminar\n0. Volver");
            Integer opcion = consola.entero("Opción");
            if (opcion == null) { continue; }
            if (opcion == 0) { return; }
            if (opcion == 1) { agregar(ruta); }
            else if (opcion == 2) { modificar(ruta); }
            else if (opcion == 3) { listarTramos(ruta); }
            else if (opcion == 4) { eliminar(ruta); }
            else { consola.error("Opción inválida."); }
        }
    }
    private void agregar(Ruta ruta) {
        String nombre = consola.texto("Nombre del tramo");
        Integer orden = consola.entero("Orden (1 a " + (ruta.getTramos().size()+1) + ")");
        if (orden == null) { return; }
        Double distancia = consola.decimal("Distancia en metros");
        if (distancia == null) { return; }
        Tramo tramo = gestor.agregarTramo(ruta.getId(), nombre, orden, distancia);
        if (tramo == null) { consola.error(gestor.getError()); return; }
        consola.exito("Tramo agregado. ID " + tramo.getId());
    }
    private void modificar(Ruta ruta) {
        Tramo tramo = elegirTramo(ruta);
        if (tramo == null) { return; }
        String nombre = consola.texto("Nuevo nombre");
        Integer orden = consola.entero("Nuevo orden");
        if (orden == null) { return; }
        Double distancia = consola.decimal("Nueva distancia");
        if (distancia == null) { return; }
        if (gestor.modificarTramo(ruta.getId(), tramo.getId(), nombre, orden, distancia)) { consola.exito("Tramo actualizado."); }
        else { consola.error(gestor.getError()); }
    }
    private void eliminar(Ruta ruta) {
        Tramo tramo = elegirTramo(ruta);
        if (tramo == null) { return; }
        if (!consola.confirmar("¿Eliminar tramo?")) { System.out.println("Operación cancelada."); return; }
        if (gestor.eliminarTramo(ruta.getId(), tramo.getId())) { consola.exito("Tramo eliminado. Orden actualizado."); }
        else { consola.error(gestor.getError()); }
    }
    private void distancia() {
        Ruta ruta = elegirRuta();
        if (ruta == null) { return; }
        double total = gestor.calcularDistanciaTotal(ruta.getId());
        if (total < 0) { consola.error(gestor.getError()); return; }
        consola.exito("Distancia total: " + consola.numero(total) + " m");
    }
}
