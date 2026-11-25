package modelo;

public class CalificacionCondiciones {
    private int iluminacion;
    private int acera;
    private int cruces;
    private int obstaculos;

    public CalificacionCondiciones(int iluminacion, int acera, int cruces, int obstaculos) {
        this.iluminacion = iluminacion;
        this.acera = acera;
        this.cruces = cruces;
        this.obstaculos = obstaculos;
    }
    public int getIluminacion() { return iluminacion; }
    public int getAcera() { return acera; }
    public int getCruces() { return cruces; }
    public int getObstaculos() { return obstaculos; }
    public int getSuma() { return iluminacion + acera + cruces + obstaculos; }
    public double calcularIndice() { return getSuma() / 20.0 * 100; }
    public int getPuntaje(String tipo) {
        if (tipo.equals("ILUMINACION")) { return iluminacion; }
        if (tipo.equals("ACERA")) { return acera; }
        if (tipo.equals("CRUCE")) { return cruces; }
        if (tipo.equals("OBSTACULO")) { return obstaculos; }
        return -1;
    }
}
