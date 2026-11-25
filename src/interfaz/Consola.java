package interfaz;
import java.util.Scanner;
import java.time.LocalDate;

public class Consola {
    private Scanner scanner = new Scanner(System.in);
    private boolean entradaCerrada = false;
    public boolean estaCerrada() { return entradaCerrada; }
    public String texto(String campo) {
        System.out.print(campo + ": ");
        if (!scanner.hasNextLine()) { entradaCerrada = true; return ""; }
        return scanner.nextLine().trim();
    }
    // Se devuelve null cuando la entrada no es válida, sin lanzar excepciones propias.
    public Integer entero(String campo) {
        String entrada = texto(campo);
        Scanner lector = new Scanner(entrada);
        Integer valor = null;
        if (lector.hasNextInt()) {
            valor = lector.nextInt();
            if (lector.hasNext()) { valor = null; }
        }
        lector.close();
        if (valor == null && !entradaCerrada) { error("Ingrese un número entero válido."); }
        return valor;
    }
    public Double decimal(String campo) {
        String entrada = texto(campo).replace(',', '.');
        Scanner lector = new Scanner(entrada);
        lector.useLocale(java.util.Locale.US);
        Double valor = null;
        if (lector.hasNextDouble()) {
            valor = lector.nextDouble();
            if (lector.hasNext()) { valor = null; }
        }
        lector.close();
        if (valor == null && !entradaCerrada) { error("Ingrese un número decimal válido."); }
        return valor;
    }
    public LocalDate fecha() {
        String entrada = texto("Fecha dd/mm/aaaa (Enter = hoy)");
        if (entradaCerrada) { return null; }
        if (entrada.isEmpty()) { return LocalDate.now(); }
        // LocalDate valida días, meses y años reales, incluidos los bisiestos.
        try {
            return LocalDate.parse(entrada, java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(java.time.format.ResolverStyle.STRICT));
        } catch (java.time.format.DateTimeParseException e) {
            error("Fecha inválida. Use dd/mm/aaaa."); return null;
        }
    }
    public boolean confirmar(String pregunta) {
        while (!entradaCerrada) {
            String respuesta = texto(pregunta + " (s/n)");
            if (respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("si") || respuesta.equalsIgnoreCase("sí")) { return true; }
            if (respuesta.equalsIgnoreCase("n") || respuesta.equalsIgnoreCase("no")) { return false; }
            if (!entradaCerrada) { error("Responda s o n."); }
        }
        return false;
    }
    public void error(String mensaje) { System.out.println("[ERROR] " + mensaje); }
    public void exito(String mensaje) { System.out.println("[OK] " + mensaje); }
    public String numero(double valor) { return String.format(java.util.Locale.US, "%.2f", valor); }
    public String tipoLugar() {
        System.out.println("1. Acceso campus | 2. Parada | 3. Estacionamiento | 4. Otro");
        Integer opcion = entero("Tipo de lugar");
        if (opcion == null) { return null; }
        if (opcion == 1) { return "ACCESO_CAMPUS"; }
        if (opcion == 2) { return "PARADA"; }
        if (opcion == 3) { return "ESTACIONAMIENTO"; }
        if (opcion == 4) { return "OTRO"; }
        error("Tipo de lugar inválido."); return null;
    }
    public String tipoReporte() {
        System.out.println("1. Iluminación | 2. Acera | 3. Cruce | 4. Obstáculo");
        Integer opcion = entero("Tipo de reporte");
        if (opcion == null) { return null; }
        if (opcion == 1) { return "ILUMINACION"; }
        if (opcion == 2) { return "ACERA"; }
        if (opcion == 3) { return "CRUCE"; }
        if (opcion == 4) { return "OBSTACULO"; }
        error("Tipo de reporte inválido."); return null;
    }
}
