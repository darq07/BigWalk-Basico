package modelo;

public class EvaluacionRuta {
    private Ruta ruta;
    private double distancia;
    private double indice;
    private String nivel;
    private int cantidadTramos;
    private int confirmados;

    public EvaluacionRuta(Ruta ruta, double distancia, double indice, String nivel, int cantidadTramos, int confirmados) {
        this.ruta = ruta;
        this.distancia = distancia;
        this.indice = indice;
        this.nivel = nivel;
        this.cantidadTramos = cantidadTramos;
        this.confirmados = confirmados;
    }
    public Ruta getRuta() { return ruta; }
    public double getDistancia() { return distancia; }
    public double getIndice() { return indice; }
    public String getNivel() { return nivel; }
    public int getCantidadTramos() { return cantidadTramos; }
    public int getConfirmados() { return confirmados; }
}
