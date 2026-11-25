package negocio;

// Clase padre: los gestores comparten el manejo de mensajes de error.
public class GestorBase {
    private String error = "";
    public String getError() { return error; }
    protected void setError(String error) { this.error = error; }
}
