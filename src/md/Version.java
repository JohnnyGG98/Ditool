package md;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Johnny
 */
public class Version {

    private int id;
    private String username;
    private String version;
    private String url;
    private String nombre;
    private String notas;
    private LocalDateTime fecha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void comprobarVersion() {
        File pv = new File(Constantes.V_DIR);
        Properties p = new Properties();
        if (pv.exists()) {
            if (comprobarRequisitos(p, pv)) {
                if (compara(p, pv)) {
                    JOptionPane.showMessageDialog(null, "Usted tiene la ultima version!");
                } else {
                    //Aqui descargamos la versio nueva

                    crearVersion();
                }
            }
        } else {
            //Aqui debemos decargar el proyecto
            crearVersion();
        }

    }

    public void crearVersion() {
        File pv = new File(Constantes.V_DIR);
        Properties p = new Properties();
        if (!pv.exists()) {
            crearPropiedades(p, pv);
        } else {
            if (pv.delete()) {
                crearPropiedades(p, pv);
            }
        }
    }

    private boolean compara(Properties p, File bd) {
        boolean igual = true;
        String pNombre = "";
        String pVersion = "";
        try {
            p.load(new FileReader(bd));
            pNombre = p.getProperty(Constantes.BD_IP);
            pVersion = p.getProperty(Constantes.BD_PUERTO);

            System.out.println("ip = " + p.getProperty(Constantes.BD_IP));
            System.out.println("database = " + p.getProperty(Constantes.BD_DATABASE));
            System.out.println("port = " + p.getProperty(Constantes.BD_PUERTO));
        } catch (IOException e) {
            System.out.println("No pudimos leer las propiedades de version: " + e.getMessage());
        }

        if (!pNombre.equals(nombre)) {
            igual = false;
        }

        if (!pVersion.equals(version)) {
            igual = false;
        }

        return igual;
    }

    private boolean crearPropiedades(Properties p, File bd) {
        boolean creado = false;
        try {
            FileOutputStream fo = new FileOutputStream(bd);
            p.setProperty(Constantes.V_AUTOR, username);
            p.setProperty(Constantes.V_NOMBRE, nombre);
            p.setProperty(Constantes.V_VERSION, version);
            p.setProperty(Constantes.V_FECHA, fecha.toString());
            p.setProperty(Constantes.V_NOTAS, notas);
            p.store(fo, "Descripcion de la version del sistema.");
            creado = true;
        } catch (FileNotFoundException e) {
            System.out.println("No podemos escribir el archivo: " + e.getMessage());
        } catch (IOException ex) {
            System.out.println("No se pudo guardar el properties: " + ex.getMessage());
        }
        return creado;
    }

    private boolean comprobarRequisitos(Properties p, File bd) {
        boolean todas = true;
        try {
            p.load(new FileReader(bd));
            if (p.getProperty(Constantes.V_AUTOR) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.V_FECHA) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.V_NOMBRE) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.V_NOTAS) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.V_VERSION) == null) {
                todas = false;
            }

        } catch (IOException e) {
            System.out.println("Divertida: " + e.getMessage());
        }
        return todas;
    }

}
