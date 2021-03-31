import java.util.ArrayList;
import java.util.Scanner;

/**
 * Interprete
 */
public class Interprete {

    // Utilidades
    Scanner sc = new Scanner(System.in);
    Vista vista = new Vista();
    Operaciones operaciones = new Operaciones();
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
                        vista.prinrErr("[!] Las intrucciones deben iniciar con '(' y finalizar con ')'");
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
        instruccion = instruccion.replace("'(", "-");

        String[] lista = instruccion.split("(?=\\()|(?=-)");

        // Descomentar para ver como estan ordenados los comandos
        // for (int i = 0; i < lista.length; i++) {
        // String tempOrden = lista[i].replace("-", "'(");
        // System.out.println(tempOrden);
        // }

        for (int i = 0; i < lista.length; i++) {

            String orden = lista[i];
            orden = orden.replace("-", "'(");

            // Agregar cada comando a una lista
            String[] comando = orden.split(" ");

            // Ver cual es el comando principal
            switch (comando[0]) {

            // VARIABLES
            case "help":

                break;

            case "write": // Imprime una expresión por consola

                // Ver que tipo de expresión va a imprimir
                try {

                    if (comando[1].split("")[0].equals("'")) { // Texto
                        vista.print(comando[1].substring(1));

                    } else if (isNumeric(comando[1])) { // Es un numero
                        vista.print(comando[1]);
                    } else { // Variable
                        Boolean existe = false;
                        String mensaje = "";
                        for (Variable variable : variables) {
                            if (variable.getNombre().equals(comando[1])) {
                                existe = true;
                                mensaje += variable.getValor() + " ";
                            }
                        }

                        vista.print(mensaje);

                        if (!existe)
                            vista.prinrErr("[!] " + comando[1] + " no esta definido como variable o funcion");
                    }
                } catch (Exception e) {

                    // Ir a traer la siguiente linea del comando
                    orden = lista[i + 1];
                    orden = orden.replace("-", "'(");
                    comando = orden.split(" ");

                    if (comando[0].split("")[0].equals("'")) {
                        vista.print(orden.trim().substring(1));
                    } // texto - en forma de lista

                    if (comando[0].split("")[0].equals("(")) { // Lista

                        // Limpiar lista
                        orden = orden.substring(0, orden.length() - 1);
                        orden = orden.substring(1);
                        String[] elementos = orden.split(" ");

                        String mensaje = "";

                        for (int j = 0; j < elementos.length; j++) {

                            // Ver si que tipo de elemento hay en la lista
                            if (elementos[j].split("")[0].equals("'")) { // texto
                                mensaje += elementos[j].substring(1) + " ";
                            } else if (isNumeric(elementos[j])) { // Expresión posfix
                                mensaje += " " + elementos[j];
                            } else { // Variable
                                Boolean existe = false;
                                for (Variable variable : variables) {
                                    if (variable.getNombre().equals(elementos[j])) {
                                        existe = true;
                                        mensaje += variable.getValor() + " ";
                                    }
                                }

                                if (!existe)
                                    vista.prinrErr("[!] " + comando[1] + " no esta definido como variable o funcion");

                            }

                        }

                        vista.print(mensaje);

                    }
                }

                break;

            case "setq": // Permite guardar y editar variables

                String nombre = comando[1];

                // Comprobar si la variable ya existe previamente
                boolean existeVariable = false;
                Variable variableExistente = null;
                for (Variable variable : variables) {
                    if (variable.getNombre().equals(nombre)) {
                        existeVariable = true;
                        variableExistente = variable;

                    }
                }
                // Ver que tipo de expresión se guardara en la variable
                try {

                    if (comando[2].split("")[0].equals("'")) { // Texto

                        if (existeVariable) {
                            variableExistente.setValor(comando[2].trim().substring(1));
                        } else {
                            variables.add(new Variable(nombre, comando[2].trim().substring(1)));

                        }

                    } else if (isNumeric(comando[2])) { // Es un numero
                        if (existeVariable) {
                            variableExistente.setValor(comando[2].trim());
                        } else {
                            variables.add(new Variable(nombre, comando[2].trim()));
                        }

                    } else { // Variable
                        Boolean existe = false;
                        for (Variable variable : variables) {
                            if (variable.getNombre().equals(comando[2])) {
                                existe = true;
                                if (existeVariable) {
                                    variableExistente.setValor(variable.getValor());
                                } else {
                                    variables.add(new Variable(nombre, variable.getValor()));
                                }
                            }
                        }

                        if (!existe)
                            vista.prinrErr("[!] " + comando[2] + " no esta definido como variable o funcion");
                    }

                } catch (Exception e) {
                    // Ir a traer la siguiente linea del comando
                    orden = lista[i + 1];
                    orden = orden.replace("-", "'(");
                    comando = orden.split(" ");

                    if (comando[0].split("")[0].equals("'")) // texto - en forma de lista
                        if (existeVariable) {
                            variableExistente.setValor(orden.trim().substring(1));
                        } else {
                            variables.add(new Variable(nombre, orden.trim().substring(1)));
                        }

                    if (comando[0].split("")[0].equals("(")) { // Lista

                        // Limpiar lista
                        orden = orden.substring(0, orden.length() - 1);
                        orden = orden.substring(1);
                        String[] elementos = orden.split(" ");

                        String mensaje = "";

                        for (int j = 0; j < elementos.length; j++) {

                            // Ver si que tipo de elemento hay en la lista
                            if (elementos[j].split("")[0].equals("'")) { // texto
                                mensaje += elementos[j].substring(1) + " ";
                            } else if (isNumeric(elementos[j])) { // Expresión posfix
                                mensaje += elementos[j] + " ";
                            } else { // Variable
                                Boolean existe = false;
                                for (Variable variable : variables) {
                                    if (variable.getNombre().equals(elementos[j])) {
                                        existe = true;
                                        mensaje += variable.getValor() + " ";
                                    }
                                }

                                if (!existe)
                                    vista.prinrErr("[!] " + comando[1] + " no esta definido como variable o funcion");

                            }

                        }

                        if (existeVariable) {
                            variableExistente.setValor(mensaje);
                        } else {
                            variables.add(new Variable(nombre, mensaje));
                        }

                    }
                }

                break;

            // LISTAS

            case "first": // Devuelve el primer elemento de una lista

                // Ver que tipo de lista es
                try {// Si es una lista dentro de una variable

                    String tempNombre = comando[1];
                    for (Variable variable : variables) {
                        if (variable.getNombre().equals(tempNombre)) {
                            if (variable.getValor().split(" ").length > 0) {
                                vista.print(variable.getValor().split(" ")[0]);
                                break;
                            } else {
                                vista.prinrErr("[!] La variable " + tempNombre + " no es una lista");
                                break;
                            }
                        }
                    }

                } catch (Exception e) { // Si se esta creando la lista
                    // Ir a traer la siguiente linea del comando
                    orden = lista[i + 1];
                    orden = orden.replace("-", "'(");
                    comando = orden.split(" ");

                    if (comando[0].split("")[0].equals("'")) {// texto - en forma de lista
                        orden = orden.trim().substring(2);
                        orden = orden.substring(0, orden.length() - 1);
                        vista.print(orden.split(" ")[0]);

                    } else if (comando[0].split("")[0].equals("(")) { // Lista

                        // Limpiar lista
                        orden = orden.substring(0, orden.length() - 1);
                        orden = orden.substring(1);
                        String elemento = orden.split(" ")[0];

                        // Ver que tipo de elemento hay en la lista
                        if (elemento.split("")[0].equals("'")) { // texto
                            vista.print(elemento.substring(1));

                        } else if (isNumeric(elemento)) { // Expresión posfix
                            vista.print(elemento);
                            break;

                        } else { // Variable
                            Boolean existe = false;
                            for (Variable variable : variables) {
                                if (variable.getNombre().equals(elemento)) {
                                    existe = true;
                                    vista.print(variable.getValor());
                                    break;
                                }
                            }

                            if (!existe) {
                                vista.prinrErr("[!] " + elemento + " no esta definido como variable o funcion");
                            }

                        }

                    }
                }

                break;
            case "rest": // Devuelve una lista sin el primer elemento
                // Ver que tipo de lista es
                try {// Si es una lista dentro de una variable

                    String tempNombre = comando[1];
                    String tempLista = "";
                    for (Variable variable : variables) {
                        if (variable.getNombre().equals(tempNombre)) {
                            if (variable.getValor().split(" ").length > 0) {

                                for (int j = 0; j < variable.getValor().split(" ").length; j++) {
                                    if (j != 0) {
                                        tempLista += variable.getValor().split(" ")[j] + " ";
                                    }
                                }
                                break;
                            } else {
                                vista.prinrErr("[!] La variable " + tempNombre + " no es una lista");
                                break;
                            }
                        }
                    }
                    vista.print("(" + tempLista + ")");

                } catch (Exception e) { // Si se esta creando la lista
                    // Ir a traer la siguiente linea del comando
                    orden = lista[i + 1];
                    orden = orden.replace("-", "'(");
                    comando = orden.split(" ");

                    if (comando[0].split("")[0].equals("'")) {// texto - en forma de lista
                        orden = orden.trim().substring(2);
                        orden = orden.substring(0, orden.length() - 1);

                        String[] tempOrder = orden.split(" ");
                        String tempLista = "";

                        for (int j = 0; j < tempOrder.length; j++) {
                            if (j != 0) {
                                tempLista += tempOrder[j] + " ";
                            }
                        }

                        vista.print(tempLista);

                    } else if (comando[0].split("")[0].equals("(")) { // Lista

                        // Limpiar lista
                        orden = orden.substring(0, orden.length() - 1);
                        orden = orden.substring(1);
                        String[] elementos = orden.split(" ");
                        String tempList = "";

                        for (int j = 0; j < elementos.length; j++) {
                            if (j != 0) {

                                // Ver que tipo de elemento hay en la lista
                                if (elementos[j].split("")[0].equals("'")) { // texto
                                    tempList += elementos[j].substring(1) + " ";

                                } else if (isNumeric(elementos[j])) { // Expresión posfix
                                    tempList += elementos[j] + " ";

                                } else { // Variable
                                    Boolean existe = false;
                                    for (Variable variable : variables) {
                                        if (variable.getNombre().equals(elementos[j])) {
                                            existe = true;
                                            tempList += variable.getValor() + " ";
                                        }
                                    }

                                    if (!existe) {
                                        vista.prinrErr(
                                                "[!] " + elementos[j] + " no esta definido como variable o funcion");
                                    }

                                }

                            }
                        }

                        vista.print(tempList);

                    }
                }

                break;

            case "second": // Devuelve el segundo elemento de una lista

                // Ver que tipo de lista es
                try {// Si es una lista dentro de una variable

                    String tempNombre = comando[1];
                    for (Variable variable : variables) {
                        if (variable.getNombre().equals(tempNombre)) {
                            if (variable.getValor().split(" ").length > 1) {
                                vista.print(variable.getValor().split(" ")[1]);
                                break;
                            } else {
                                vista.prinrErr("[!] No existen suficientes elementos en la lista");
                                break;
                            }
                        }
                    }

                } catch (Exception e) { // Si se esta creando la lista
                    // Ir a traer la siguiente linea del comando
                    orden = lista[i + 1];
                    orden = orden.replace("-", "'(");
                    comando = orden.split(" ");

                    if (comando[0].split("")[0].equals("'")) {// texto - en forma de lista
                        orden = orden.trim().substring(2);
                        orden = orden.substring(0, orden.length() - 1);
                        if (orden.split(" ").length > 1) {
                            vista.print(orden.split(" ")[1]);
                        } else {
                            vista.prinrErr("[!] No existen suficientes elementos en la lista");
                        }

                    } else if (comando[0].split("")[0].equals("(")) { // Lista

                        // Limpiar lista
                        orden = orden.substring(0, orden.length() - 1);
                        orden = orden.substring(1);
                        String elemento = orden.split(" ")[1];

                        // Ver que tipo de elemento hay en la lista
                        if (elemento.split("")[0].equals("'")) { // texto
                            if (orden.split(" ").length >= 1) {
                                vista.print(orden.split(" ")[1].substring(1));
                            } else {
                                vista.prinrErr("[!] No existen suficientes elementos en la lista");
                            }
                            break;
                        } else if (isNumeric(elemento)) { // Expresión posfix
                            if (orden.split(" ").length > 1) {
                                vista.print(elemento);
                            } else {
                                vista.prinrErr("[!] No existen suficientes elementos en la lista");
                            }
                            break;

                        } else { // Variable
                            Boolean existe = false;
                            for (Variable variable : variables) {
                                if (variable.getNombre().equals(elemento)) {
                                    existe = true;
                                    vista.print(variable.getValor());
                                    break;
                                }
                            }

                            if (!existe) {
                                vista.prinrErr("[!] " + elemento + " no esta definido como variable o funcion");
                            }

                        }

                    }
                }

                break;

            case "thirtd":// Devuelve el tercer elemento de una lista

                // Ver que tipo de lista es
                try {// Si es una lista dentro de una variable

                    String tempNombre = comando[1];
                    for (Variable variable : variables) {
                        if (variable.getNombre().equals(tempNombre)) {
                            if (variable.getValor().split(" ").length > 2) {
                                vista.print(variable.getValor().split(" ")[2]);
                                break;
                            } else {
                                vista.prinrErr("[!] No existen suficientes elementos en la lista");
                                break;
                            }
                        }
                    }

                } catch (Exception e) { // Si se esta creando la lista
                    // Ir a traer la siguiente linea del comando
                    orden = lista[i + 1];
                    orden = orden.replace("-", "'(");
                    comando = orden.split(" ");

                    if (comando[0].split("")[0].equals("'")) {// texto - en forma de lista
                        orden = orden.trim().substring(2);
                        orden = orden.substring(0, orden.length() - 1);
                        if (orden.split(" ").length > 2) {
                            vista.print(orden.split(" ")[2]);
                        } else {
                            vista.prinrErr("[!] No existen suficientes elementos en la lista");
                        }

                    } else if (comando[0].split("")[0].equals("(")) { // Lista

                        // Limpiar lista
                        orden = orden.substring(0, orden.length() - 1);
                        orden = orden.substring(1);
                        String elemento = orden.split(" ")[2];

                        // Ver que tipo de elemento hay en la lista
                        if (elemento.split("")[0].equals("'")) { // texto
                            if (orden.split(" ").length > 2) {
                                vista.print(orden.split(" ")[2].substring(1));
                            } else {
                                vista.prinrErr("[!] No existen suficientes elementos en la lista");
                            }
                            break;
                        } else if (isNumeric(elemento)) { // Expresión posfix
                            if (orden.split(" ").length > 2) {
                                vista.print(elemento);
                            } else {
                                vista.prinrErr("[!] No existen suficientes elementos en la lista");
                            }
                            break;

                        } else { // Variable
                            Boolean existe = false;
                            for (Variable variable : variables) {
                                if (variable.getNombre().equals(elemento)) {
                                    existe = true;
                                    vista.print(variable.getValor());
                                    break;
                                }
                            }

                            if (!existe) {
                                vista.prinrErr("[!] " + elemento + " no esta definido como variable o funcion");
                            }

                        }

                    }
                }

                break;

            case "nth": // Devuelve el N elemento de una lista

                // Ver que tipo de lista es
                try {// Si es una lista dentro de una variable

                    String tempNombre = comando[2];
                    int index = Integer.parseInt(comando[1]);
                    try {
                        for (Variable variable : variables) {
                            if (variable.getNombre().equals(tempNombre)) {
                                if (variable.getValor().split(" ").length >= index) {
                                    vista.print(variable.getValor().split(" ")[index - 1]);
                                    break;
                                } else {
                                    vista.prinrErr("[!] No existen suficientes elementos en la lista");
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        vista.prinrErr("[!] La posicion " + index + " no es valida");
                    }

                } catch (Exception e) { // Si se esta creando la lista

                    int index = Integer.parseInt(comando[1]);

                    // Ir a traer la siguiente linea del comando
                    orden = lista[i + 1];
                    orden = orden.replace("-", "'(");
                    comando = orden.split(" ");

                    if (comando[0].split("")[0].equals("'")) {// texto - en forma de lista
                        orden = orden.trim().substring(2);
                        orden = orden.substring(0, orden.length() - 1);
                        if (orden.split(" ").length >= index) {
                            vista.print(orden.split(" ")[index - 1]);
                        } else {
                            vista.prinrErr("[!] No existen suficientes elementos en la lista");
                        }

                    } else if (comando[0].split("")[0].equals("(")) { // Lista

                        // Limpiar lista
                        orden = orden.substring(0, orden.length() - 1);
                        orden = orden.substring(1);
                        String elemento = orden.split(" ")[index - 1];

                        // Ver que tipo de elemento hay en la lista
                        if (elemento.split("")[0].equals("'")) { // texto
                            if (orden.split(" ").length >= index) {
                                vista.print(orden.split(" ")[index - 1].substring(1));
                            } else {
                                vista.prinrErr("[!] No existen suficientes elementos en la lista");
                            }
                            break;
                        } else if (isNumeric(elemento)) { // Expresión posfix
                            if (orden.split(" ").length >= index) {
                                vista.print(elemento);
                            } else {
                                vista.prinrErr("[!] No existen suficientes elementos en la lista");
                            }
                            break;

                        } else { // Variable
                            Boolean existe = false;
                            for (Variable variable : variables) {
                                if (variable.getNombre().equals(elemento)) {
                                    existe = true;
                                    vista.print(variable.getValor());
                                    break;
                                }
                            }

                            if (!existe) {
                                vista.prinrErr("[!] " + elemento + " no esta definido como variable o funcion");
                            }

                        }

                    }
                }

                break;

            case "cons": // Agrega un elemento al principio de una lista y la regresa

                String listaCompleta = "";

                if (comando[1].split("")[0].equals("'")) { // Texto
                    listaCompleta += comando[1].substring(1) + " ";

                } else if (isNumeric(comando[1])) { // Es un numero
                    vista.print(comando[1]);
                    listaCompleta += comando[1] + " ";

                } else { // Variable
                    Boolean existe = false;
                    String mensaje = "";
                    for (Variable variable : variables) {
                        if (variable.getNombre().equals(comando[1])) {
                            existe = true;
                            mensaje += variable.getValor() + " ";
                        }
                    }

                    listaCompleta += mensaje;

                    if (!existe) {
                        vista.prinrErr("[!] " + comando[1] + " no esta definido como variable o funcion");
                    }
                }

                try {
                    // Ir a traer la siguiente linea del comando
                    orden = lista[i + 1];
                    orden = orden.replace("-", "'(");
                    comando = orden.split(" ");

                    if (comando[0].split("")[0].equals("'")) { // texto - en forma de lista

                        listaCompleta += orden.trim().substring(1) + " ";

                    } else if (comando[0].split("")[0].equals("(")) { // Lista

                        // Limpiar lista
                        orden = orden.substring(0, orden.length() - 1);
                        orden = orden.substring(1);
                        String[] elementos = orden.split(" ");

                        String mensaje = "";

                        for (int j = 0; j < elementos.length; j++) {

                            // Ver si que tipo de elemento hay en la lista
                            if (elementos[j].split("")[0].equals("'")) { // texto
                                mensaje += elementos[j].substring(1) + " ";
                            } else if (isNumeric(elementos[j])) { // Expresión posfix
                                mensaje += " " + elementos[j];
                            } else { // Variable
                                Boolean existe = false;
                                for (Variable variable : variables) {
                                    if (variable.getNombre().equals(elementos[j])) {
                                        existe = true;
                                        mensaje += variable.getValor() + " ";
                                    }
                                }

                                if (!existe)
                                    vista.prinrErr("[!] " + comando[0] + " no esta definido como variable o funcion");

                            }

                        }

                        listaCompleta += mensaje;
                    }
                } catch (Exception e) {
                    vista.prinrErr("[!] Error en sintaxis cerca de 'cons'. Debe ingresar una lista como parametro");
                    break;
                }

                listaCompleta = listaCompleta.replace(")", "");
                vista.print(listaCompleta);

                break;

            case "append": // Devuelve la union de dos listas

                listaCompleta = "";

                int n = 1;
                while (n <= 2) {

                    try {
                        // Ir a traer la siguiente linea del comando
                        orden = lista[i + n];
                        orden = orden.replace("-", "'(");
                        comando = orden.split(" ");

                        if (comando[0].split("")[0].equals("'")) { // texto - en forma de lista

                            listaCompleta += orden.trim().substring(1) + " ";

                        } else if (comando[0].split("")[0].equals("(")) { // Lista

                            // Limpiar lista
                            orden = orden.substring(0, orden.length() - 1);
                            orden = orden.substring(1);
                            String[] elementos = orden.split(" ");

                            String mensaje = "";

                            for (int j = 0; j < elementos.length; j++) {

                                // Ver si que tipo de elemento hay en la lista
                                if (elementos[j].split("")[0].equals("'")) { // texto
                                    mensaje += elementos[j].substring(1) + " ";
                                } else if (isNumeric(elementos[j])) { // Expresión posfix
                                    mensaje += elementos[j] + " ";
                                } else { // Variable
                                    Boolean existe = false;
                                    for (Variable variable : variables) {
                                        if (variable.getNombre().equals(elementos[j])) {
                                            existe = true;
                                            mensaje += variable.getValor() + " ";
                                        }
                                    }

                                    if (!existe) {
                                        vista.prinrErr(
                                                "[!] " + comando[0] + " no esta definido como variable o funcion");
                                    }

                                }

                            }

                            listaCompleta += mensaje;

                        }
                        n += 1;
                    } catch (Exception e) {
                        vista.prinrErr("[!] Error en sintaxis cerca de 'cons'. Debe ingresar una lista como parametro");
                        break;
                    }

                }

                listaCompleta = listaCompleta.replace(")", "");
                vista.print(listaCompleta);

                break;

            case "list": // Construye una lista con los elementos que se le pasan

                listaCompleta = "";
                for (int j = 0; j < comando.length; j++) {
                    if (j != 0) {

                        if (comando[j].split("")[0].equals("'")) { // Texto
                            listaCompleta += comando[j].substring(1) + " ";

                        } else if (isNumeric(comando[j])) { // Es un numero
                            listaCompleta += comando[j] + " ";

                        } else { // Variable
                            Boolean existe = false;
                            for (Variable variable : variables) {
                                if (variable.getNombre().equals(comando[1])) {
                                    existe = true;
                                    listaCompleta += variable.getValor() + " ";
                                }
                            }

                            if (!existe)
                                vista.prinrErr("[!] " + comando[1] + " no esta definido como variable o funcion");
                        }

                    }
                }

                vista.print(listaCompleta);

                break;

            case "last": // devuelve el ultimo elemento de una lista

                // Ver que tipo de lista es
                try {// Si es una lista dentro de una variable

                    String tempNombre = comando[1];
                    for (Variable variable : variables) {
                        if (variable.getNombre().equals(tempNombre)) {
                            if (variable.getValor().split(" ").length > 0) {
                                int ultimaPos = variable.getValor().split(" ").length - 1;
                                vista.print(variable.getValor().split(" ")[ultimaPos]);
                                break;
                            } else {
                                vista.prinrErr("[!] La variable " + tempNombre + " no es una lista");
                                break;
                            }
                        }
                    }

                } catch (Exception e) { // Si se esta creando la lista
                    // Ir a traer la siguiente linea del comando
                    orden = lista[i + 1];
                    orden = orden.replace("-", "'(");
                    comando = orden.split(" ");

                    if (comando[0].split("")[0].equals("'")) {// texto - en forma de lista
                        orden = orden.trim().substring(2);
                        orden = orden.substring(0, orden.length() - 1);
                        vista.print(orden.split(" ")[-1]);

                    } else if (comando[0].split("")[0].equals("(")) { // Lista

                        // Limpiar lista
                        orden = orden.substring(0, orden.length() - 1);
                        orden = orden.substring(1);

                        int ultimaPos = orden.split(" ").length - 1;

                        String elemento = orden.split(" ")[ultimaPos];

                        // Ver que tipo de elemento hay en la lista
                        if (elemento.split("")[0].equals("'")) { // texto
                            vista.print(elemento.substring(1));

                        } else if (isNumeric(elemento)) { // Expresión posfix
                            vista.print(elemento);
                            break;

                        } else { // Variable
                            Boolean existe = false;
                            for (Variable variable : variables) {
                                if (variable.getNombre().equals(elemento)) {
                                    existe = true;
                                    vista.print(variable.getValor());
                                    break;
                                }
                            }

                            if (!existe) {
                                vista.prinrErr("[!] " + elemento + " no esta definido como variable o funcion");
                            }

                        }

                    }
                }

                break;

            // FUNCIONES

            case "defun":
                break;

            // CONDICIONALES

            case "ecuals":
                break;

            case "eval":
                break;

            case "cond":
                break;

            // PREDICADOS

            case "null":
                break;

            case "atom":
                break;

            case "numberp":
                break;

            }

        }

    }

    // UTILIDADES

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

    /**
     * Valida si un texto es un numero o no
     * 
     * @param string
     * @return True si es un numero y false si no
     */
    public static boolean isNumeric(String string) {

        if (string == null || string.equals("")) {
            return false;
        }

        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

}