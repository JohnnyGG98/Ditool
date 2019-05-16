package ctr;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Johnny
 */
public class CMD {

    public static void ejecutarJAR(String nombre) {
        //Corremos el programa desde consola
        try {
            String cmd = "nuevo/" + nombre + "/" + nombre + ".jar";
            //String d = "../pruebas-jar/dist/Ejemplo-PF.jar";
            File jar = new File(cmd);
            if (jar.exists()) {
                String cmda = "java -jar " + cmd;
                Runtime.getRuntime().exec(cmda);
                System.out.println("Pasamos: ");
            } else {
                JOptionPane.showMessageDialog(null, "Permisos denegados \n"
                        + "ejecute el programa manualmente.");
            }
        } catch (IOException e) {
            System.out.println("No se pudo ejecutar: " + e.getMessage());
        }

    }
}
