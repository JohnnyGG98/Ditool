package ctr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;

/**
 *
 * @author Johnny
 */
public class Descarga {

    private final String url;
    private final String nombre;
    private final String carpeta;
    private boolean todoBien;

    public Descarga(String url, String nombre, String carpeta) {
        this.url = url;
        this.nombre = nombre;
        this.carpeta = carpeta;
    }

    public void descargar() {
        comprobarCarpeta();
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
    }

    private void comprobarCarpeta() {
        File dir = new File(nombre);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Creamos la carpeta");
            }
        }
    }

    private void descargando() {
        File fil = new File(carpeta + nombre);
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            todoBien = false;
            JOptionPane.showMessageDialog(null, "La descarga va a comenzar ...");

            InputStream i = conn.getInputStream();
            OutputStream o = new FileOutputStream(fil);

            int b = 0;
            while (b != -1) {
                b = i.read();
                System.out.println("Descagando: " + b);
                if (b != -1) {
                    o.write(b);
                }
            }
            System.out.println("");
            System.out.println("Terminamos de descargar");

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
