package utils;

import java.io.File;

/**
 *
 * @author alumno
 */
public class C {

    //Modo del proyecto 
    public static final boolean M_DESARROLLO = true;
    // Driver de postgres
    public static final String DRIVER_POSTGRES = "org.postgresql.Driver";
    //Nombre de las propiedadesde la base de matos
    public static final String BD_DATABASE = "database",
            BD_IP = "ip",
            BD_PUERTO = "port";
    //Direccion de las propiedades de base de datos
    public static final String BD_DIR = "configuracion.properties";
    //Nombre de las propiedades la version
    public static final String V_AUTOR = "Autor", V_NOMBRE = "Nombre", V_VERSION = "Version",
            V_NOTAS = "Notas", V_FECHA = "Fecha";
    //Direccion de las propiedades de version  
    public static final String V_DIR = "version.properties";

    //Seteamos los valores de la base de datos
    public static String url = null;
    public static String user = null;
    public static String pass = null;

    //Obtenemos la direccion 
    public static String getDir() {
        File dir = new File("./");
        return dir.getAbsolutePath();
    }

}
