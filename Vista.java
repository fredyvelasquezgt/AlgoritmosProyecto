public class Vista {

    String red = "\033[31m";
    String green = "\033[32m";
    String reset = "\u001B[0m";

    public void prinrErr(String mensaje) {
        System.out.print(red + mensaje + reset + "\n");
    }

    public void print(String mensaje) {
        System.out.print(green + mensaje + reset + "\n");
    }

}
