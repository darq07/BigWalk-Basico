package negocio;

public class Validador {
    public static boolean texto(String texto, int minimo, int maximo) {
        return texto != null && texto.trim().length() >= minimo && texto.trim().length() <= maximo;
    }
    public static boolean rango(int valor, int minimo, int maximo) {
        return valor >= minimo && valor <= maximo;
    }
    public static boolean distancia(double valor) {
        return valor >= 0.01 && valor <= 5000;
    }
    public static String normalizar(String texto) {
        if (texto == null) { return ""; }
        String limpio = texto.trim().toLowerCase(java.util.Locale.ROOT);
        limpio = limpio.replace('á', 'a').replace('é', 'e').replace('í', 'i');
        limpio = limpio.replace('ó', 'o').replace('ú', 'u').replace('ü', 'u');
        String resultado = "";
        for (int i = 0; i < limpio.length(); i++) {
            char letra = limpio.charAt(i);
            if (Character.isWhitespace(letra)) {
                if (!resultado.isEmpty() && !resultado.endsWith(" ")) { resultado += " "; }
            } else { resultado += letra; }
        }
        return resultado.trim();
    }
    public static double redondear(double valor) { return Math.round(valor * 100.0) / 100.0; }
    public static boolean tipoLugar(String tipo) {
        return "ACCESO_CAMPUS".equals(tipo) || "PARADA".equals(tipo) || "ESTACIONAMIENTO".equals(tipo) || "OTRO".equals(tipo);
    }
    public static boolean tipoReporte(String tipo) {
        return "ILUMINACION".equals(tipo) || "ACERA".equals(tipo) || "CRUCE".equals(tipo) || "OBSTACULO".equals(tipo);
    }
}
