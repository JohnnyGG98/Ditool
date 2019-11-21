package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import model.Version;

/**
 *
 * @author Johnny
 */
public class Descarga {

    private final Version version;
    private boolean todoBien;

    public Descarga(Version version) {
        this.version = version;
    }

    public boolean descargar() {
        //comprobarCarpeta();
        descargando();
        if (todoBien) {
            JOptionPane.showMessageDialog(null, "Descargamos todo correctamente, \n"
                    + "Ahora instalaremos el programa.");
        } else {
            int r = JOptionPane.showConfirmDialog(null, "Tuvimos algunas complicaciones en la descarga \n"
                    + "¿Desea intentarlo nuevamente?");
            if (r == JOptionPane.YES_OPTION) {
                descargar();
            }
        }
        return todoBien;
    }

    private void descargando() {
        File fil = new File(version.getNombre());
        try {
            URLConnection conn = new URL(version.getUrl()).openConnection();
            conn.connect();
            todoBien = false;
            JOptionPane.showMessageDialog(null, "La descarga va a comenzar...");

            InputStream i = conn.getInputStream();
            OutputStream o = new FileOutputStream(fil);

            int b = 0;
            while (b != -1) {
                b = i.read();
                if (b != -1) {
                    o.write(b);
                }
            }

            o.close();
            i.close();
            todoBien = true;
        } catch (IOException e) {
            System.out.println("ELE " + e.getMessage());
            JOptionPane.showMessageDialog(null, "No pudimos conectarnos para realizar la descarga, \n"
                    + "compruebe su conexion a internet.");
        }

    }

}
