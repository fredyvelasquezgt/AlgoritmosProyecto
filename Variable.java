
public class Variable {
    private String valor;
    private String nombre;

    public Variable(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public String toString() {
        return (valor);
    }
}