package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Johnny
 */
public abstract class Utils {

    //Obtenemos la direccion 
    public static String getDir() {
        File dir = new File("./");
        return dir.getAbsolutePath();
    }

    public static String generarURL() {
        File bd = new File(C.BD_DIR);
        Properties p = new Properties();
        comprobarArchivo(bd, p);
        String ip = "";
        String port = "";
        String database = "";
        try {
            p.load(new FileReader(bd));
            ip = p.getProperty(C.BD_IP);
            port = p.getProperty(C.BD_PUERTO);
            database = p.getProperty(C.BD_DATABASE);
        } catch (IOException e) {
            System.out.println("No encontramos el archivo. " + e.getMessage());
        }
        return "jdbc:postgresql://" + ip + ":" + port + "/" + database;
    }

    public static String getUserBD() {
        return "tsds";
    }

    public static String getPassBD() {
        return "TDSoftware158";
    }

    private static void comprobarArchivo(File bd, Properties p) {
        if (!bd.exists()) {
            crearPropiedades(p, bd);
        }

        if (!comprobarRequisitos(p, bd)) {
            if (bd.delete()) {
                System.out.println("No tiene todas las propiedades lo borramos.");
            }
            crearPropiedades(p, bd);
        }
    }

    private static boolean comprobarRequisitos(Properties p, File bd) {
        boolean todas = true;
        try {
            p.load(new FileReader(bd));
            if (p.getProperty(C.BD_DATABASE) == null) {
                todas = false;
            }

            if (p.getProperty(C.BD_IP) == null) {
                todas = false;
            }

            if (p.getProperty(C.BD_PUERTO) == null) {
                todas = false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "No tenemos todos los requisitos para "
                    + "conectarnos a la base de datos.\n"
                    + e.getMessage()
            );
        }
        return todas;
    }

    /**
     * Creamos las propiedades para poder conectarnos a la base de datos
     *
     * @param p
     * @param bd
     * @return
     */
    private static boolean crearPropiedades(Properties p, File bd) {
        boolean creado = false;
        try {
            FileOutputStream fo = new FileOutputStream(bd);
            p.setProperty(C.BD_IP, "localhost");
            p.setProperty(C.BD_DATABASE, "bd");
            p.setProperty(C.BD_PUERTO, "5432");
            p.store(
                    fo, "Propiedades de la base de datos: \n"
                    + "Modifiquelos para que se pueda "
                    + "conectarse a su base de datos: "
            );
            creado = true;
        } catch (FileNotFoundException e) {
            System.out.println("No podemos escribir el archivo: " + e.getMessage());
        } catch (IOException ex) {
            System.out.println("No se pudo guardar el properties: " + ex.getMessage());
        }
        return creado;
    }

}
