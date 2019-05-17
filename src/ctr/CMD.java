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
            String cmd = nombre + ".jar";
            File jar = new File(cmd);
            System.out.println(cmd);
            String cmda = "java -jar " + jar.getAbsolutePath();
            System.out.println(cmda);
            Runtime.getRuntime().exec(cmda);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Permisos denegados \n"
                    + "ejecute el programa manualmente.");
        }

    }
}
