package interfaz;
import modelo.*;
import negocio.GestorReportes;
import java.time.LocalDate;
import java.util.ArrayList;

public class MenuReportes {
    private Consola consola;
    private GestorReportes gestor;
    private MenuRutas menuRutas;
    public MenuReportes(Consola consola, GestorReportes gestor, MenuRutas menuRutas) {
        this.consola = consola; this.gestor = gestor; this.menuRutas = menuRutas;
    }
    public void ejecutar() {
        while (!consola.estaCerrada()) {
            System.out.println("\nMÓDULO 2 - REPORTES\n1. Registrar reporte\n2. Buscar duplicado\n3. Actualizar estado\n4. Listar reportes\n0. Volver");
            Integer opcion = consola.entero("Opción");
            if (opcion == null) { continue; }
            if (opcion == 0) { return; }
            if (opcion == 1) { registrar(); }
            else if (opcion == 2) { consultarDuplicado(); }
            else if (opcion == 3) { actualizar(); }
            else if (opcion == 4) { listar(); }
            else { consola.error("Opción inválida."); }
        }
    }
    private void registrar() {
        Ruta ruta = menuRutas.elegirRuta(); if (ruta == null) { return; }
        Tramo tramo = menuRutas.elegirTramo(ruta); if (tramo == null) { return; }
        String tipo = consola.tipoReporte(); if (tipo == null) { return; }
        String descripcion = consola.texto("Descripción");
        LocalDate fecha = consola.fecha(); if (fecha == null) { return; }
        Integer gravedad = consola.entero("Gravedad (1-5)"); if (gravedad == null) { return; }
        if (!negocio.Validador.texto(descripcion,5,200) || !negocio.Validador.rango(gravedad,1,5)) { consola.error("Descripción de 5 a 200 caracteres y gravedad de 1 a 5."); return; }
        Reporte duplicado = gestor.buscarDuplicado(ruta.getId(), tramo.getId(), tipo, fecha);
        if (!gestor.getError().isEmpty()) { consola.error(gestor.getError()); return; }
        boolean continuar = false;
        if (duplicado != null) {
            System.out.println("[AVISO] Posible duplicado: " + duplicado);
            continuar = consola.confirmar("¿Registrar de todas formas?");
            if (!continuar) { System.out.println("Registro cancelado."); return; }
        }
        Reporte reporte = gestor.registrarReporte(ruta.getId(), tramo.getId(), tipo, descripcion, fecha, gravedad, continuar);
        if (reporte == null) { consola.error(gestor.getError()); return; }
        consola.exito("Reporte " + reporte.getId() + " registrado: " + reporte.getEstado());
    }
    private void consultarDuplicado() {
        Ruta ruta = menuRutas.elegirRuta(); if (ruta == null) { return; }
        Tramo tramo = menuRutas.elegirTramo(ruta); if (tramo == null) { return; }
        String tipo = consola.tipoReporte(); if (tipo == null) { return; }
        LocalDate fecha = consola.fecha(); if (fecha == null) { return; }
        Reporte reporte = gestor.buscarDuplicado(ruta.getId(), tramo.getId(), tipo, fecha);
        if (!gestor.getError().isEmpty()) { consola.error(gestor.getError()); }
        else if (reporte == null) { System.out.println("No hay reportes activos similares."); }
        else { System.out.println("[AVISO] Posible duplicado: " + reporte); }
    }
    private void actualizar() {
        imprimir(gestor.getReportes());
        if (gestor.getReportes().isEmpty()) { return; }
        Integer id = consola.entero("ID de reporte"); if (id == null) { return; }
        System.out.println("1. Pendiente | 2. Confirmado | 3. Cerrado");
        Integer opcion = consola.entero("Nuevo estado"); if (opcion == null) { return; }
        String estado = "";
        if (opcion == 1) { estado = "PENDIENTE"; }
        if (opcion == 2) { estado = "CONFIRMADO"; }
        if (opcion == 3) { estado = "CERRADO"; }
        if (gestor.actualizarEstado(id, estado)) { consola.exito("Reporte " + id + " actualizado a " + estado); }
        else { consola.error(gestor.getError()); }
    }
    private void listar() {
        String filtro = consola.texto("ID de tramo (Enter = todos)");
        if (consola.estaCerrada()) { return; }
        if (filtro.isEmpty()) { imprimir(gestor.getReportes()); return; }
        java.util.Scanner lector = new java.util.Scanner(filtro);
        if (!lector.hasNextInt()) { lector.close(); consola.error("ID de tramo inválido."); return; }
        int id = lector.nextInt();
        if (lector.hasNext()) { lector.close(); consola.error("ID de tramo inválido."); return; }
        lector.close();
        ArrayList<Reporte> lista = gestor.listarPorTramo(id);
        if (lista == null) { consola.error(gestor.getError()); } else { imprimir(lista); }
    }
    private void imprimir(ArrayList<Reporte> lista) {
        if (lista.isEmpty()) { System.out.println("No hay reportes."); }
        for (Reporte reporte : lista) { System.out.println(reporte); }
    }
}
