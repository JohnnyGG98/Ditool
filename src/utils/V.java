package utils;

/**
 *
 * @author gus
 */
public class V {

    public static boolean esUrlZip(String entrada) {
        return entrada.matches("https://+[-a-zA-Z0-9+&@#/%?=~_|!:,.;]+[a-zA-Z0-9]+\\.zip+")
                || entrada.matches("https://+[-a-zA-Z0-9+&@#/%?=~_|!:,.;]+\\.zip+")
                || entrada.matches("http://+[-a-zA-Z0-9+&@#/%?=~_|!:,.;]+\\.zip+");
    }

    public static boolean esLetrasSimple(String entrada) {
        return entrada.matches("[a-zA-Z0-9\\.\\-\\_]+");
    }

    public static boolean esVersion(String entrada) {
        return entrada.matches("[0-9]{1,3}+\\.+[0-9]{1,2}") || entrada.matches("[0-9]{1}+\\.+[0-9]{1,2}+\\.+[0-9]{1,2}");
    }

    public static boolean esDescripcion(String entrada) {
        return entrada.matches("[A-Za-záéíóúÁÉÍÓÚÑñkK\\,\\s\\/\\:\\.\\-0-9]+");
    }

}
