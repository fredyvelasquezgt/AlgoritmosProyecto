import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Interprete
 */
public class Interprete {

    // Utilidades
    Scanner sc = new Scanner(System.in);
    Vista vista = new Vista();
    String instruccion = "";

    // Variables
    ArrayList<Variable> variables = new ArrayList<Variable>();

    /*
     * Pide las intrucciones por consola y las ejecuta
     */
    public void ejecutar() {

        Boolean estado = true;
        while (estado) {

            if (instruccion.length() > 0) {
                System.out.print(">> ... ");
            } else {
                System.out.print(">> ");
            }
            String tempInstruccion = sc.nextLine().toLowerCase();
            instruccion += " " + tempInstruccion;

            int parentesisApertura = contarCaracteres(instruccion, '(');
            int parentesisCierre = contarCaracteres(instruccion, ')');

            // Ver si se ha completado la intruccion
            if (parentesisApertura == parentesisCierre) {

                if (parentesisCierre > 0) {
                    evaluar(instruccion);
                } else {
                    vista.prinrErr(">> [!] Las intrucciones deben iniciar con '(' y finalizar con ')'");
                }

                instruccion = "";
            }

        }

    }

    /*
     * Interpreta la instruccion y ejecuta lo necesario para realizarla
     * 
     * @param instruccion
     */
    public void evaluar(String instruccion) {

        // Limpiar cadena
        instruccion = instruccion.trim();
        instruccion = instruccion.substring(0, instruccion.length() - 1);
        instruccion = instruccion.substring(1);

        // Preparar la cadena con cada comando
        // String[] lista = instruccion.split("(?=\\())");

        instruccion = instruccion.replace("'(", "-");
        String[] lista = instruccion.split("(?=\\()|(?=-)");
        for (int i = 0; i < lista.length; i++) {

            String comando = lista[i];
            comando = comando.replace("-", "'(");


            // Descomentar para ver como estan ordenados los comandos
            // System.out.println(comando);

            // Ver el comando principal
            switch (comando.split(" ")[0]) {

            // Variables
            case "exit":
                break;

            case "help":
                break;

            case "write":
                break;

            case "setq":
                break;

            case "first":
                break;

            case "rest":
                break;

            case "second":
                break;

            case "thritd":
                break;

            case "nth":
                break;

            // Listas

            case "cons":
                break;

            case "append":
                break;

            case "list":
                break;

            case "last":
                break;

            case "reverse":
                break;

            // Funciones

            case "defun":
                break;

            // Condicionales

            case "ecuals":
                break;

            case "eval":
                break;

            case "cond":
                break;

            // Predicados

            case "null":
                break;

            case "atom":
                break;

            case "numberp":
                break;

            }

        }

    }

    /*
     * Guarda una variable en el programa
     * 
     * @param comando
     */
    public void guardar(String nombre, String valor) {

    }

    /*
     * método para calcular el número de veces que se repite un carácter en un
     * String
     * 
     * @param instruccion
     */
    public static int contarCaracteres(String cadena, char caracter) {
        int posicion, contador = 0;
        // se busca la primera vez que aparece
        posicion = cadena.indexOf(caracter);
        while (posicion != -1) { // mientras se encuentre el caracter
            contador++; // se cuenta
            // se sigue buscando a partir de la posición siguiente a la encontrada
            posicion = cadena.indexOf(caracter, posicion + 1);
        }
        return contador;
    }

}