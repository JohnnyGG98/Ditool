package controller;

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
            String cmd = nombre + ".jar";
            File jar = new File(cmd);
            String cmda = "java -jar " + jar.getAbsolutePath();
            Runtime.getRuntime().exec(cmda);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Permisos denegados \n"
                    + "ejecute el programa manualmente.");
        }

    }
}
