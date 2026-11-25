import modelo.*;
import negocio.*;

public class PruebasDuplicados {
    private static int total;
    private static int fallos;
    private static void verificar(String nombre, boolean bien) {
        total++;
        if (!bien) { fallos++; System.out.println("FALLO: " + nombre); }
    }
    private static Ruta crear(GestorRutas g, String nombre) {
        return g.registrarRuta(nombre, "Campus", "ACCESO_CAMPUS", "Parada", "PARADA");
    }
    public static void main(String[] args) {
        GestorRutas g = new GestorRutas();
        Ruta a = crear(g, "Ruta principal");
        verificar("Nombre normalizado", crear(g, " RÚTA  PRINCIPAL ") == null && g.getRutas().size() == 1);
        verificar("Vacia no confirma", !g.confirmarRuta(a.getId()) && !a.estaConfirmada());
        g.agregarTramo(a.getId(), "Avenida norte", 1, 100);
        verificar("Confirmar original", g.confirmarRuta(a.getId()));
        Ruta b = crear(g, "Alternativa");
        verificar("Mismos extremos permitidos", b != null && b.getId() == 2);
        Tramo b1 = g.agregarTramo(b.getId(), " AVÉNIDA  NORTE ", 1, 200);
        verificar("Prefijo permitido en borrador", b1 != null);
        verificar("Duplicado aunque cambie distancia", !g.confirmarRuta(b.getId()) && g.getError().contains("duplicado"));
        verificar("Confirmacion fallida conserva borrador", !b.estaConfirmada() && b.getTramos().size() == 1 && b1.getDistancia() == 200);
        Tramo b2 = g.agregarTramo(b.getId(), "Calle lateral", 2, 50);
        verificar("Alternativa extendida", b2 != null && g.confirmarRuta(b.getId()));
        verificar("Eliminar no crea duplicado", !g.eliminarTramo(b.getId(), b2.getId()) && b.getTramos().size() == 2 && b2.getOrden() == 2);
        int idSiguiente = b2.getId() + 1;
        verificar("Agregar no crea duplicado", g.agregarTramo(a.getId(), "Calle lateral", 2, 60) == null && a.getTramos().size() == 1);
        Tramo nuevo = g.agregarTramo(a.getId(), "Sendero nuevo", 2, 60);
        verificar("Rechazo no consume ID", nuevo != null && nuevo.getId() == idSiguiente);
        verificar("Modificar atomico", !g.modificarTramo(a.getId(), nuevo.getId(), "Calle lateral", 2, 999) && nuevo.getNombre().equals("Sendero nuevo") && nuevo.getDistancia() == 60);
        Ruta c = crear(g, "Orden inverso");
        g.agregarTramo(c.getId(), "Calle lateral", 1, 50);
        Tramo c2 = g.agregarTramo(c.getId(), "Avenida norte", 2, 100);
        verificar("Orden distinto permitido", g.confirmarRuta(c.getId()));
        verificar("Reordenar atomico", !g.modificarTramo(c.getId(), c2.getId(), "Avenida norte", 1, 999) && c2.getOrden() == 2 && c2.getDistancia() == 100);
        verificar("Reconfirmar propia ruta", g.confirmarRuta(c.getId()));
        Ruta inversa = g.registrarRuta("Sentido inverso", "Parada", "PARADA", "Campus", "ACCESO_CAMPUS");
        g.agregarTramo(inversa.getId(), "Avenida norte", 1, 100);
        verificar("Extremos invertidos distintos", g.confirmarRuta(inversa.getId()));
        verificar("Ultimo tramo no se elimina", !g.eliminarTramo(inversa.getId(), inversa.getTramos().get(0).getId()));
        SistemaBigWalk s = new SistemaBigWalk();
        DatosEjemplo.cargar(s);
        verificar("Ejemplos confirmados", s.getGestorRutas().buscarRuta(1).estaConfirmada() && s.getGestorRutas().buscarRuta(2).estaConfirmada());
        verificar("Ejemplo 27.40", s.getEvaluador().evaluarRuta(1).getIndice() == 27.40);
        System.out.println("Duplicados: " + (total-fallos) + "/" + total + " correctas");
        if (fallos > 0) { System.exit(1); }
    }
}
