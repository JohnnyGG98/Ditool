package ctr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Johnny
 */
public class PActualiza {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws net.lingala.zip4j.exception.ZipException {
        String url = "https://github.com/JohnnyGG98/PF-Restaurante-M3A/releases/latest";
        String nombre = "is.txt";

        String carpeta = "nuevo/";

        File dir = new File(carpeta);

        if (!dir.exists()) {
            if (!dir.mkdir()) {
                System.out.println("No se pudo crear la carpeta.");
            } else {
                System.out.println("Creamos la carpeta");
            }
        }

        File fil = new File(carpeta + nombre);
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            System.out.println("Empezamos la descarga.");

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
        } catch (IOException e) {
            System.out.println("No nos pudimos conectar: " + e.getMessage());
        }

    }

}
