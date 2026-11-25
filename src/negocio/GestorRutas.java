package negocio;
import modelo.*;
import java.util.ArrayList;

public class GestorRutas extends GestorBase {
    private ArrayList<Ruta> rutas = new ArrayList<Ruta>();
    private ArrayList<Lugar> lugares = new ArrayList<Lugar>();
    private int siguienteRuta = 1;
    private int siguienteTramo = 1;

    public ArrayList<Ruta> getRutas() { return new ArrayList<Ruta>(rutas); }
    private Lugar buscarLugar(String nombre) {
        for (Lugar lugar : lugares) {
            if (Validador.normalizar(lugar.getNombre()).equals(Validador.normalizar(nombre))) { return lugar; }
        }
        return null;
    }
    public Ruta registrarRuta(String nombre, String origen, String tipoOrigen, String destino, String tipoDestino) {
        setError("");
        if (!Validador.texto(nombre, 3, 60) || !Validador.texto(origen, 3, 60) || !Validador.texto(destino, 3, 60)) {
            setError("Los nombres deben tener entre 3 y 60 caracteres."); return null;
        }
        if (!Validador.tipoLugar(tipoOrigen) || !Validador.tipoLugar(tipoDestino)) { setError("Tipo de lugar inválido."); return null; }
        if (Validador.normalizar(origen).equals(Validador.normalizar(destino))) { setError("Origen y destino deben ser distintos."); return null; }
        for (Ruta ruta : rutas) {
            if (Validador.normalizar(ruta.getNombre()).equals(Validador.normalizar(nombre))) { setError("Ya existe una ruta con ese nombre."); return null; }
        }
        Lugar inicio = buscarLugar(origen);
        Lugar fin = buscarLugar(destino);
        if ((inicio != null && !inicio.getTipo().equals(tipoOrigen)) || (fin != null && !fin.getTipo().equals(tipoDestino))) {
            setError("Un lugar registrado debe conservar su tipo."); return null;
        }
        if (inicio == null) { inicio = new Lugar(origen.trim(), tipoOrigen); lugares.add(inicio); }
        if (fin == null) { fin = new Lugar(destino.trim(), tipoDestino); lugares.add(fin); }
        Ruta ruta = new Ruta(siguienteRuta, nombre.trim(), inicio, fin);
        siguienteRuta++;
        rutas.add(ruta);
        return ruta;
    }
    public Ruta buscarRuta(int id) {
        setError("");
        for (Ruta ruta : rutas) { if (ruta.getId() == id) { return ruta; } }
        setError("No existe la ruta con ID " + id + "."); return null;
    }
    public Tramo buscarTramo(int idRuta, int idTramo) {
        Ruta ruta = buscarRuta(idRuta);
        if (ruta == null) { return null; }
        for (Tramo tramo : ruta.getTramos()) { if (tramo.getId() == idTramo) { return tramo; } }
        setError("El tramo " + idTramo + " no existe en la ruta seleccionada."); return null;
    }
    public Tramo buscarTramoGeneral(int idTramo) {
        setError("");
        for (Ruta ruta : rutas) {
            for (Tramo tramo : ruta.getTramos()) { if (tramo.getId() == idTramo) { return tramo; } }
        }
        setError("No existe el tramo con ID " + idTramo + "."); return null;
    }
    private boolean validarTramo(Ruta ruta, int idIgnorado, String nombre, int orden, double distancia, int maxOrden) {
        if (!Validador.texto(nombre, 3, 60)) { setError("Nombre de tramo: 3 a 60 caracteres."); return false; }
        if (!Validador.distancia(distancia)) { setError("Distancia válida: 0.01 a 5000 metros."); return false; }
        if (!Validador.rango(orden, 1, maxOrden)) { setError("Orden válido: 1 a " + maxOrden + "."); return false; }
        for (Tramo tramo : ruta.getTramos()) {
            if (tramo.getId() != idIgnorado && Validador.normalizar(tramo.getNombre()).equals(Validador.normalizar(nombre))) {
                setError("Nombre de tramo repetido."); return false;
            }
        }
        return true;
    }
    public Tramo agregarTramo(int idRuta, String nombre, int orden, double distancia) {
        Ruta ruta = buscarRuta(idRuta);
        if (ruta == null || !validarTramo(ruta, -1, nombre, orden, distancia, ruta.getTramos().size() + 1)) { return null; }
        Tramo tramo = new Tramo(siguienteTramo, nombre.trim(), orden, distancia);
        siguienteTramo++;
        ruta.agregarTramo(tramo, orden);
        return tramo;
    }
    public boolean modificarTramo(int idRuta, int idTramo, String nombre, int orden, double distancia) {
        Tramo tramo = buscarTramo(idRuta, idTramo);
        if (tramo == null) { return false; }
        Ruta ruta = buscarRuta(idRuta);
        if (!validarTramo(ruta, idTramo, nombre, orden, distancia, ruta.getTramos().size())) { return false; }
        tramo.setNombre(nombre.trim());
        tramo.setDistancia(distancia);
        ruta.moverTramo(tramo, orden);
        return true;
    }
    public boolean eliminarTramo(int idRuta, int idTramo) {
        Tramo tramo = buscarTramo(idRuta, idTramo);
        if (tramo == null) { return false; }
        if (!tramo.getReportes().isEmpty()) { setError("No se puede eliminar un tramo con reportes."); return false; }
        buscarRuta(idRuta).eliminarTramo(tramo);
        return true;
    }

    public double calcularDistanciaTotal(int idRuta) {
        Ruta ruta = buscarRuta(idRuta);
        if (ruta == null) { return -1; }
        ArrayList<Tramo> tramos = ruta.getTramos();
        if (tramos.isEmpty()) { setError("La ruta no tiene tramos. Agregue al menos uno."); return -1; }
        double total = 0;
        for (int i = 0; i < tramos.size(); i++) {
            Tramo tramo = tramos.get(i);
            if (tramo == null) { setError("Tramo inexistente en posición " + (i+1)); return -1; }
            if (tramo.getId() <= 0 || tramo.getOrden() != i + 1 || !Validador.distancia(tramo.getDistancia()) || !Validador.texto(tramo.getNombre(), 3, 60)) {
                setError("Datos inconsistentes en tramo " + tramo.getId() + ". Revise ID, nombre, orden o distancia."); return -1;
            }
            for (int j = 0; j < i; j++) {
                Tramo anterior = tramos.get(j);
                if (anterior.getId() == tramo.getId() || Validador.normalizar(anterior.getNombre()).equals(Validador.normalizar(tramo.getNombre()))) {
                    setError("ID o nombre repetido en tramo " + tramo.getId() + "."); return -1;
                }
            }
            total += tramo.getDistancia();
        }
        return total;
    }
}
