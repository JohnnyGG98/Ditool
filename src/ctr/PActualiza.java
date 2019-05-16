package ctr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import md.ConectarDB;
import md.Version;

/**
 *
 * @author Johnny
 */
public class PActualiza {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //ConectarDB conecta = new ConectarDB("johnny", "DEV");
        Version v = new Version();
        v.setFecha(LocalDateTime.MIN);
        v.setId(0);
        v.setNombre("P-ALARMA");
        v.setNotas("En esta version corregimos el error 1200.");
        v.setUrl("www.misuperproyecto.com/p.zip");
        v.setUsername("Master");
        v.setVersion("1.0.1");
        
        System.out.println("Ahora compararemos: ");
        v.comprobarVersion();

//        String url = "https://github.com/JohnnyGG98/PF-Restaurante-M3A/releases/latest";
//        String nombre = "is.txt";
//
//        String carpeta = "nuevo/";
//
//        File dir = new File(carpeta);
//
//        if (!dir.exists()) {
//            if (!dir.mkdir()) {
//                System.out.println("No se pudo crear la carpeta.");
//            } else {
//                System.out.println("Creamos la carpeta");
//            }
//        }
//
//        File fil = new File(carpeta + nombre);
//        try {
//            URLConnection conn = new URL(url).openConnection();
//            conn.connect();
//            System.out.println("Empezamos la descarga.");
//
//            InputStream i = conn.getInputStream();
//            OutputStream o = new FileOutputStream(fil);
//
//            int b = 0;
//            while (b != -1) {
//                b = i.read();
//                System.out.println("Descagando: " + b);
//                if (b != -1) {
//                    o.write(b);
//                }
//            }
//            System.out.println("");
//            System.out.println("Terminamos de descargar");
//
//            o.close();
//            i.close();
//        } catch (IOException e) {
//            System.out.println("No nos pudimos conectar: " + e.getMessage());
//        }
    }

}
