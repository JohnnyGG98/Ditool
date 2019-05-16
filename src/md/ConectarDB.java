package md;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author alumno
 */
public class ConectarDB {

    private Connection ct;

    public ConectarDB(String user, String pass) {
        try {
            String url = generarURL();
            //Cargamos el driver
            Class.forName("org.postgresql.Driver");
            ct = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            System.out.println("No pudimos conectarnos DB. " + e.getMessage());
        } catch (SQLException ex) {
            System.out.println("No nos pudimos conectar. " + ex.getMessage());
        }
    }

    public PreparedStatement getPs(String nsql) {
        try {
            PreparedStatement ps = ct.prepareStatement(nsql);
            return ps;
        } catch (SQLException e) {
            System.out.println("No se pudo preparar el statement. " + e.getMessage());
            return null;
        }
    }

    public ResultSet sql(PreparedStatement ps) {
        try {
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("No se pudo ejecutar el sql: " + e.getMessage());
            return null;
        }
    }

    public void cerrar(PreparedStatement ps) {
        try {
            ps.getConnection().close();
        } catch (SQLException e) {
            System.out.println("No pudimos cerrar la conexion pls try again. " + e.getMessage());
        }
    }

    private String generarURL() {
        File bd = new File(Constantes.BD_DIR);
        Properties p = new Properties();
        if (!bd.exists()) {
            crearPropiedades(p, bd);
        }

        if (!comprobarRequisitos(p, bd)) {
            if (bd.delete()) {
                System.out.println("No tiene todas las propiedades lo borramos.");
            }
            crearPropiedades(p, bd);
        }

        String ip = "";
        String port = "";
        String database = "";

        try {
            p.load(new FileReader(bd));
            ip = p.getProperty(Constantes.BD_IP);
            port = p.getProperty(Constantes.BD_PUERTO);
            database = p.getProperty(Constantes.BD_DATABASE);

            System.out.println("ip = " + p.getProperty(Constantes.BD_IP));
            System.out.println("database = " + p.getProperty(Constantes.BD_DATABASE));
            System.out.println("port = " + p.getProperty(Constantes.BD_PUERTO));
        } catch (IOException e) {
            System.out.println("No encontramos el señor archivo. " + e.getMessage());
        }

        return "jdbc:postgresql://" + ip + ":" + port + "/" + database;
    }

    private boolean comprobarRequisitos(Properties p, File bd) {
        boolean todas = true;
        try {
            p.load(new FileReader(bd));
            if (p.getProperty(Constantes.BD_DATABASE) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.BD_IP) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.BD_PUERTO) == null) {
                todas = false;
            }

        } catch (IOException e) {
            System.out.println("Divertida: " + e.getMessage());
        }
        return todas;
    }

    private boolean crearPropiedades(Properties p, File bd) {
        boolean creado = false;
        try {
            FileOutputStream fo = new FileOutputStream(bd);
            p.setProperty("ip", "Escribirmos el fichero nuevamente");
            p.setProperty("database", "Este es un super valor");
            p.setProperty("port", "Este es un super valor");
            p.store(fo, "Propiedades de la base de datos");
            creado = true;
        } catch (FileNotFoundException e) {
            System.out.println("No podemos escribir el archivo: " + e.getMessage());
        } catch (IOException ex) {
            System.out.println("No se pudo guardar el properties: " + ex.getMessage());
        }
        return creado;
    }

}
