package interfaz;
import negocio.SistemaBigWalk;
import negocio.DatosEjemplo;

public class BigWalkApp {
    public static void main(String[] args) {
        Consola consola = new Consola();
        SistemaBigWalk sistema = new SistemaBigWalk();
        if (consola.confirmar("¿Cargar datos de ejemplo?")) { DatosEjemplo.cargar(sistema); }
        MenuPrincipal menu = new MenuPrincipal(consola, sistema);
        menu.ejecutar();
        System.out.println("Fin del programa.");
    }
}
