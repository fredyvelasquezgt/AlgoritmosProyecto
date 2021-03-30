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

            if (instruccion.equals(" (exit)")) {
                estado = false;
            } else {

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
        String[] lista = instruccion.split(" ");
        for (int i = 0; i < lista.length; i++) {

            String comando = lista[i];

            if (comando != "") {
                System.out.println(comando.trim().toLowerCase());

                switch (comando) {
                case "setq": // Definir una variable (setq <nombre> <valor>)
                    try {
                        variables.add(new Variable(lista[i + 1], ""));

                    } catch (Exception e) {
                        vista.prinrErr("[!] Sintaxis incorrecta en el comando SETQ");
                    }

                    break;

                default:
                    break;
                }

            }
        }

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