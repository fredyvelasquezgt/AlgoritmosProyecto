

public class Funciones {

    private String parametros;
    private String hacer;

    /**
     * @post: constructor vacio de la clase
     */
    public Funciones() {

    }

    /**
     * @post: constructor de la clase
     * @param hacer define que es lo que hace la funcion creada.
     * @param parametros define el parametro que la funcion necesita para trabajar
     */
    public Funciones(String hacer,String parametros) {
        this.hacer = hacer;
        this.parametros = parametros;
    }

    /**
     * @post: separa por partes el input enviado para identificar lo que hara la funcion y sus parametros necesario.
     * @param input input que sera evaluado para identificar las diferentes partes de la funcion.
     * @return regresa el nombre de la funcion como un primer valor y en el segundo las instrucciones de lo que hace.
     */
    public Funciones Defun(String input) {
        String[] temp = input.split(" ");
        StringBuilder eve = new StringBuilder();
        //System.out.println(Arrays.toString(temp));
        for (int i = 6; i < temp.length; i++) {
            try {
                char c = input.charAt(i);

                if(Character.isLetterOrDigit(c)|| temp[4].equals(temp[i])){

                    if(temp[i].equals("(") || temp[i].equals(")") || temp[i].equals("=") || temp[i].equals("-") || temp[i].equals("+") || temp[i].equals("*") || temp[i].equals("/")|| temp[i].equals("<") || temp[i].equals(">") ){
                        eve.append(" ");
                        eve.append(temp[i]);
                        eve.append(" ");
                    }else{

                        eve.append(" ");
                        eve.append(temp[i]);
                        eve.append(" ");
                    }
                }
                else{
                    verifica(temp, eve, i);
                }
            }catch (Exception e){
                verifica(temp, eve, i);
            }

        }
        return new Funciones(eve.toString(),temp[4]);
    }

    /**
     * @post: verifica que por cada uno de los caracteres especiales exista un espacio en blanco antes y despues del mismo.
     * @param temp el caracteres que se debe evaluar.
     * @param eve contenedor en el que se agregara el valor resultante si es que el caracter evaluado no tenia los espacios en blanco correspondientes.
     * @param i posicion en la que se debe evaluar el caracter.
     */
    private void verifica(String[] temp, StringBuilder eve, int i) {
        if(temp[i].equals("(") || temp[i].equals(")") || temp[i].equals("=") || temp[i].equals("-") || temp[i].equals("+") || temp[i].equals("*") || temp[i].equals("/")|| temp[i].equals("<") || temp[i].equals(">") ){

            eve.append(" ");
            eve.append(temp[i]);
            eve.append(" ");
        }else{
            eve.append(temp[i]);
        }
    }

    /**
     * @post: metodo que cambia el parametro que la funcion necesita por el valor indicado.
     * @param x el valor por el cual se debe sustituir el parametro dentro de la funcion.
     * @return regresa las instrucciones de la funcion con el parametro cambiado.
     */
    public String loquehace(String x){

        String[] temp = hacer.split(" ");
        StringBuilder eve = new StringBuilder();
        for (int i = 0; i < temp.length; i++){
            if(temp[i].equals(parametros)){
                temp[i] = x;
            }
            eve.append(temp[i]);
            eve.append(" ");
        }

        return eve.toString();
    }

    /**
     * @return regresa las instrucciones de la funcion indicada.
     */
    @Override
    public String toString() {
        return (hacer);
    }
}