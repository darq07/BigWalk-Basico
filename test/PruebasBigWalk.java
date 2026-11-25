import modelo.*;
import negocio.*;
import java.time.LocalDate;

public class PruebasBigWalk {
    private static int correctas = 0;
    private static int fallidas = 0;
    private static void comprobar(String nombre, boolean condicion) {
        if (condicion) { correctas++; }
        else { fallidas++; System.out.println("FALLO: " + nombre); }
    }
    public static void main(String[] args) {
        SistemaBigWalk sistema = new SistemaBigWalk();
        GestorRutas r = sistema.getGestorRutas();
        GestorReportes p = sistema.getGestorReportes();
        EvaluadorRiesgo e = sistema.getEvaluador();
        LocalDate hoy = LocalDate.now();
        comprobar("Nombre corto", r.registrarRuta("ab", "Campus", "ACCESO_CAMPUS", "Parada", "PARADA") == null);
        comprobar("Extremos iguales", r.registrarRuta("Ruta A", "Cámpus", "ACCESO_CAMPUS", "campus", "ACCESO_CAMPUS") == null);
        comprobar("Tipo inválido", r.registrarRuta("Ruta A", "Campus", "INVALIDO", "Parada", "PARADA") == null);
        Ruta a = r.registrarRuta("Ruta A", "Campus", "ACCESO_CAMPUS", "Parada", "PARADA");
        comprobar("Ruta válida", a != null && a.getId() == 1);
        comprobar("Nombre duplicado", r.registrarRuta(" RÚTA  A ", "Campus", "ACCESO_CAMPUS", "Parada", "PARADA") == null);
        comprobar("Tipo incompatible", r.registrarRuta("Ruta B", "Campus", "OTRO", "Parada", "PARADA") == null);
        Ruta b = r.registrarRuta("Ruta B", "Campus", "ACCESO_CAMPUS", "Parada", "PARADA");
        comprobar("Lugar reutilizado", a.getOrigen() == b.getOrigen());
        comprobar("Ruta inexistente", r.calcularDistanciaTotal(99) == -1);
        comprobar("Ruta vacía", r.calcularDistanciaTotal(1) == -1);
        comprobar("Distancia cero", r.agregarTramo(1,"Tramo A",1,0) == null);
        comprobar("Distancia negativa", r.agregarTramo(1,"Tramo A",1,-3) == null);
        comprobar("Distancia infinita", r.agregarTramo(1,"Tramo A",1,Double.POSITIVE_INFINITY) == null);
        comprobar("Distancia NaN", r.agregarTramo(1,"Tramo A",1,Double.NaN) == null);
        comprobar("Distancia excesiva", r.agregarTramo(1,"Tramo A",1,5001) == null);
        comprobar("Orden inválido", r.agregarTramo(1,"Tramo A",2,120) == null);
        Tramo t1 = r.agregarTramo(1,"Tramo A",1,120);
        Tramo t2 = r.agregarTramo(1,"Tramo B",2,300);
        Tramo t3 = r.agregarTramo(1,"Tramo C",3,80);
        Tramo t4 = r.agregarTramo(2,"Tramo D",1,400);
        comprobar("Tramos creados", t1 != null && t2 != null && t3 != null && t4 != null);
        comprobar("Nombre tramo duplicado", r.agregarTramo(1,"trámo a",4,100) == null);
        comprobar("Tramo de otra ruta", r.buscarTramo(1,t4.getId()) == null);
        comprobar("Total exacto", r.calcularDistanciaTotal(1) == 500);
        comprobar("Mover tramo", r.modificarTramo(1,t3.getId(),"Tramo C",1,80) && t3.getOrden() == 1 && t1.getOrden() == 2);
        comprobar("Modificar inválido conserva datos", !r.modificarTramo(1,t1.getId(),"Otro nombre",2,-1) && t1.getNombre().equals("Tramo A"));
        comprobar("Eliminar tramo", r.eliminarTramo(1,t3.getId()) && a.getTramos().size() == 2 && t1.getOrden() == 1);
        t3 = r.agregarTramo(1,"Tramo C",3,80);
        t1.setDistancia(-1);
        comprobar("Inconsistencia detectada", r.calcularDistanciaTotal(1) == -1);
        t1.setDistancia(120);
        comprobar("Tipo reporte inválido", p.registrarReporte(1,t1.getId(),"X","Descripción válida",hoy,3,false) == null);
        comprobar("Descripción corta", p.registrarReporte(1,t1.getId(),"ACERA","a",hoy,3,false) == null);
        comprobar("Gravedad inválida", p.registrarReporte(1,t1.getId(),"ACERA","Descripción válida",hoy,6,false) == null);
        comprobar("Fecha futura", p.registrarReporte(1,t1.getId(),"ACERA","Descripción válida",hoy.plusDays(1),3,false) == null);
        comprobar("Fecha antigua", p.registrarReporte(1,t1.getId(),"ACERA","Descripción válida",hoy.minusDays(366),3,false) == null);
        comprobar("Duplicado con fecha inválida", p.buscarDuplicado(1,t1.getId(),"ACERA",hoy.plusDays(1)) == null && !p.getError().isEmpty());
        comprobar("Reporte otra ruta", p.registrarReporte(1,t4.getId(),"ACERA","Descripción válida",hoy,3,false) == null);
        Reporte rep = p.registrarReporte(1,t2.getId(),"ILUMINACION","Luminaria dañada",hoy.minusDays(7),4,false);
        comprobar("Reporte pendiente", rep != null && rep.getEstado().equals("PENDIENTE"));
        comprobar("Misma referencia", p.getReportes().get(0) == t2.getReportes().get(0));
        comprobar("Duplicado a siete días", p.buscarDuplicado(1,t2.getId(),"ILUMINACION",hoy) == rep);
        comprobar("No duplicado a ocho días", p.buscarDuplicado(1,t2.getId(),"ILUMINACION",hoy.minusDays(15)) == null && p.getError().isEmpty());
        comprobar("Duplicado cancelado", p.registrarReporte(1,t2.getId(),"ILUMINACION","Otra luminaria",hoy,4,false) == null);
        comprobar("Duplicado confirmado", p.registrarReporte(1,t2.getId(),"ILUMINACION","Otra luminaria",hoy,4,true) != null);
        comprobar("Estado inválido", !p.actualizarEstado(rep.getId(),"X"));
        comprobar("Confirmar reporte", p.actualizarEstado(rep.getId(),"CONFIRMADO"));
        comprobar("No repetir estado", !p.actualizarEstado(rep.getId(),"CONFIRMADO"));
        comprobar("No regresar a pendiente", !p.actualizarEstado(rep.getId(),"PENDIENTE"));
        comprobar("No eliminar tramo con reporte", !r.eliminarTramo(1,t2.getId()));
        comprobar("Ruta incompleta", e.evaluarRuta(1) == null && e.getError().contains("Tramos sin calificación"));
        comprobar("Índice sin calificación", e.calcularIndiceTramo(1,t1.getId()) == -1);
        comprobar("Calificación fuera de rango", !e.calificarTramo(1,t1.getId(),6,0,0,0));
        comprobar("Calificación en otra ruta", !e.calificarTramo(1,t4.getId(),1,1,1,1));
        comprobar("Calificar tramo", e.calificarTramo(1,t1.getId(),1,0,1,0));
        comprobar("Índice individual", e.calcularIndiceTramo(1,t1.getId()) == 10);
        comprobar("Actualizar tramo", e.calificarTramo(1,t1.getId(),2,1,3,0) && e.calcularIndiceTramo(1,t1.getId()) == 30);
        comprobar("Conservar calificación anterior", !e.calificarTramo(1,t1.getId(),-1,0,0,0) && e.calcularIndiceTramo(1,t1.getId()) == 30);
        e.calificarTramo(1,t1.getId(),1,0,1,0);
        e.calificarTramo(1,t2.getId(),0,1,3,1);
        comprobar("Advertencia de coherencia", e.advertencias(t2).size() == 1);
        e.calificarTramo(1,t2.getId(),2,1,3,1);
        e.calificarTramo(1,t3.getId(),0,1,4,0);
        EvaluacionRuta evaluacion = e.evaluarRuta(1);
        comprobar("Índice global ponderado", evaluacion != null && evaluacion.getIndice() == 27.40);
        comprobar("Confirmados no alteran fórmula", evaluacion.getConfirmados() == 1);
        comprobar("Clasificación baja", evaluacion.getNivel().equals("BAJA"));
        comprobar("Límite baja", e.calcularNivel(33.33).equals("BAJA"));
        comprobar("Límite media", e.calcularNivel(66.66).equals("MEDIA"));
        comprobar("Límite alta", e.calcularNivel(66.67).equals("ALTA"));
        comprobar("Cerrar confirmado", p.actualizarEstado(rep.getId(),"CERRADO"));
        comprobar("No reabrir cerrado", !p.actualizarEstado(rep.getId(),"CONFIRMADO"));
        comprobar("Cerrar pendiente", p.actualizarEstado(2,"CERRADO"));
        comprobar("Cerrados no duplican", p.buscarDuplicado(1,t2.getId(),"ILUMINACION",hoy) == null);
        comprobar("Cerrados no cuentan", e.evaluarRuta(1).getConfirmados() == 0);
        comprobar("Cerrados conservan índice", e.evaluarRuta(1).getIndice() == 27.40);
        comprobar("Tramo inexistente al listar", p.listarPorTramo(999) == null);
        comprobar("Filtrar reportes", p.listarPorTramo(t2.getId()).size() == 2);
        System.out.println("Pruebas correctas: " + correctas + " | Fallidas: " + fallidas);
        if (fallidas > 0) { System.exit(1); }
    }
}
