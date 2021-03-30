public class Operaciones {


    public Operaciones() {

    }


    public String Operar(String datos){
        String[] temp = datos.split(" ");
        int res;
        try {
           res = Integer.parseInt(temp[1]);
            switch (temp[0]){
                case  "+":
                    for (int i = 2; i < temp.length; i++) {
                        if(!temp[i].equals(" ")){
                            res += Integer.parseInt(temp[i]);
                        }
                    }
                    break;
                case  "-":
                    for (int i = 2; i < temp.length; i++) {
                        if(!temp[i].equals(" ")){
                            res -= Integer.parseInt(temp[i]);
                        }
                    }
                    break;
                case  "*":
                    for (int i = 2; i < temp.length; i++) {
                        if(!temp[i].equals(" ")){
                            res = res *Integer.parseInt(temp[i]);
                        }
                    }
                    break;
                case  "/":
                    for (int i = 2; i < temp.length; i++) {
                        if(!temp[i].equals(" ")){
                            res= res / Integer.parseInt(temp[i]);
                        }
                    }
                    break;
            }
        } catch (Exception e){
            return datos;
        }

        return String.valueOf(res);
    }


}